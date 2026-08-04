package com.uisrael.prototipogestalabweb.controller;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.response.LoginResponseDto;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.uisrael.prototipogestalabweb.model.dto.request.EmpleadoRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.FirmaElectronicaRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.UsuarioRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.UsuariohasRolRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.AreaResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CargoResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EmpleadoResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.FirmaElectronicaResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.RolResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.UsuarioResponseDto;
import com.uisrael.prototipogestalabweb.services.IAreaService;
import com.uisrael.prototipogestalabweb.services.ICargoService;
import com.uisrael.prototipogestalabweb.services.IEmpleadoService;
import com.uisrael.prototipogestalabweb.services.IFirmaElectronicaService;
import com.uisrael.prototipogestalabweb.services.IRolService;
import com.uisrael.prototipogestalabweb.services.IUsuarioService;
import com.uisrael.prototipogestalabweb.services.IUsuariohasRolService;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {
	
	private final IEmpleadoService empleadoService;
    private final IAreaService areaService;
    private final ICargoService cargoService;
    private final IUsuarioService usuarioService;
    private final IFirmaElectronicaService firmaElectronicaService;
    private final IRolService rolService;
    private final IUsuariohasRolService usuariohasRolService;

	public EmpleadoController(IEmpleadoService empleadoService, IAreaService areaService, ICargoService cargoService,
			IUsuarioService usuarioService, IFirmaElectronicaService firmaElectronicaService, IRolService rolService,
			IUsuariohasRolService usuariohasRolService) {
		super();
		this.empleadoService = empleadoService;
		this.areaService = areaService;
		this.cargoService = cargoService;
		this.usuarioService = usuarioService;
		this.firmaElectronicaService = firmaElectronicaService;
		this.rolService = rolService;
		this.usuariohasRolService = usuariohasRolService;
	}

	 @GetMapping("/listar")
	    public String listarEmpleados(Model model) {
	            List<EmpleadoResponseDto> empleadosBD = empleadoService.listarEmpleados();
	            model.addAttribute("empleados", empleadosBD);
	            return "empleado/listarempleado";
	    }

	    // Show create employee form
	    @GetMapping("/nuevo")
	    public String mostrarFormularioNuevo(Model model) {
	            // La fecha de ingreso la fija el sistema: se muestra pero no se elige.
	            EmpleadoRequestDto empleadoNuevo = new EmpleadoRequestDto();
	            empleadoNuevo.setFechaIngreso(new Date());
	            model.addAttribute("empleado", empleadoNuevo);

	            List<AreaResponseDto> areas = areaService.listarAreas();
	            List<CargoResponseDto> cargos = cargoService.listarCargos();
	            List<RolResponseDto> roles = rolService.listarRoles();
	            model.addAttribute("areas", areas);
	            model.addAttribute("cargos", cargos);
	            model.addAttribute("roles", roles);
	            
	            return "empleado/nuevoempleado";
	    }

	    // Save new employee (creates Usuario + UsuariohasRol + FirmaElectronica first, then links them)
	    @PostMapping("/guardar")
	    public String guardarEmpleado(
	    				@ModelAttribute EmpleadoRequestDto empleado,
	    				@ModelAttribute("usuarioNombre") String usuarioNombre,
	    				@ModelAttribute("usuarioContrasenia") String usuarioContrasenia,
	    				@ModelAttribute("fkRol") int fkRol,
	    				@ModelAttribute("firmaFormato") String firmaFormato,
	    				@DateTimeFormat(pattern = "yyyy-MM-dd")
	    				@ModelAttribute("firmaFechaSubida") Date firmaFechaSubida,
	    				@DateTimeFormat(pattern = "yyyy-MM-dd")
	    				@ModelAttribute("firmaFechaExpiracion") Date firmaFechaExpiracion,
	    				@ModelAttribute("usuarioCorreoLaboral") String usuarioCorreoLaboral,
	    				Model model) {

	    	try {
	    		// La fecha de ingreso siempre es la del dia en que se registra.
	    		empleado.setFechaIngreso(new Date());

	    		// Las fechas se comprueban ANTES de crear nada. Si se dejara para el
	    		// final, cada rechazo dejaria una cuenta de usuario, una asignacion de
	    		// rol y una firma electronica huerfanas en la base.
	    		String avisoFechas = errorEnFechasDelEmpleado(empleado);
	    		if (avisoFechas != null) {
	    			return mostrarAviso(model, empleado, avisoFechas);
	    		}

	    		String avisoFirma = errorEnFechasDeLaFirma(firmaFechaSubida, firmaFechaExpiracion);
	    		if (avisoFirma != null) {
	    			return mostrarAviso(model, empleado, avisoFirma);
	    		}

	    		// La cedula se comprueba ANTES de crear la cuenta de usuario, el rol y
	    		// la firma. Si se dejara para el final, esos tres registros quedarian
	    		// creados y huerfanos cada vez que el backend rechaza el empleado.
	    		if (cedulaYaRegistrada(empleado.getCi(), 0)) {
	    			return mostrarCedulaDuplicada(model, empleado);
	    		}

	    		// 1. Create the user account
	    		UsuarioRequestDto nuevoUsuario = new UsuarioRequestDto();
	    		nuevoUsuario.setNombre(usuarioNombre);
	    		nuevoUsuario.setCorreo(usuarioCorreoLaboral);
	    		nuevoUsuario.setContrasenia(usuarioContrasenia);
	    		nuevoUsuario.setFechaCreacion(new Date());
	    		nuevoUsuario.setEstadoUsuario(true);
	    		UsuarioResponseDto usuarioGuardado = usuarioService.guardarUsuarios(nuevoUsuario);

	    		// 2. Assign the role to that user
	    		UsuariohasRolRequestDto usuarioRol = new UsuariohasRolRequestDto();
	    		usuarioRol.setFkUsuario(usuarioGuardado.getIdUsuario());
	    		usuarioRol.setFkRol(fkRol);
	    		usuarioRol.setFechaAsignacion(new Date());
	    		usuariohasRolService.guardarUsuariohasRol(usuarioRol);

	    		// 3. Create the electronic signature
	    		FirmaElectronicaRequestDto nuevaFirma = new FirmaElectronicaRequestDto();
	    		nuevaFirma.setFormatoFirma(firmaFormato);
	    		nuevaFirma.setFechaSubida(firmaFechaSubida != null ? firmaFechaSubida : new Date());
	    		nuevaFirma.setFechaExpiracion(firmaFechaExpiracion);
	    		FirmaElectronicaResponseDto firmaGuardada = firmaElectronicaService.guardarFirmas(nuevaFirma);

	    		// 4. Link the generated IDs and save the employee
	    		empleado.setFkUsuario(usuarioGuardado.getIdUsuario());
	    		empleado.setFkFirmaElectronica(firmaGuardada.getIdFirma());
	    		empleadoService.guardarEmpleados(empleado);

	    		return "redirect:/empleado/listar?success=true";

	    	} catch (WebClientResponseException.Conflict ex) {
	    		return mostrarErrorDuplicado(model, empleado, ex);
	    	}
	    }
	    
	    @GetMapping("/editar/{id}")
	    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
	        try {
	        	EmpleadoResponseDto empleadoActual = empleadoService.buscarPorId(id);

	        	EmpleadoRequestDto empleadoForm = new EmpleadoRequestDto();
	        	empleadoForm.setIdEmpleado(empleadoActual.getIdEmpleado());
	        	empleadoForm.setNombre(empleadoActual.getNombre());
	        	empleadoForm.setApellido(empleadoActual.getApellido());
	        	empleadoForm.setCi(empleadoActual.getCi());
	        	empleadoForm.setCorreo(empleadoActual.getCorreo());
	        	empleadoForm.setDireccion(empleadoActual.getDireccion());
	        	empleadoForm.setFechaIngreso(empleadoActual.getFechaIngreso());
	        	empleadoForm.setFechaSalida(empleadoActual.getFechaSalida());
	        	empleadoForm.setEstadoEmpleado(empleadoActual.isEstadoEmpleado());
	        	if (empleadoActual.getFkArea() != null) {
	        		empleadoForm.setFkArea(empleadoActual.getFkArea().getIdArea());
	        	}
	        	if (empleadoActual.getFkCargo() != null) {
	        		empleadoForm.setFkCargo(empleadoActual.getFkCargo().getIdCargo());
	        	}
	        	if (empleadoActual.getFkUsuario() != null) {
	        		empleadoForm.setFkUsuario(empleadoActual.getFkUsuario().getIdUsuario());
	        	}
	        	if (empleadoActual.getFkFirmaElectronica() != null) {
	        		empleadoForm.setFkFirmaElectronica(empleadoActual.getFkFirmaElectronica().getIdFirma());
	        	}

	            List<AreaResponseDto> areas = areaService.listarAreas();
	            List<CargoResponseDto> cargos = cargoService.listarCargos();
	            
	            model.addAttribute("empleado", empleadoForm);
	            model.addAttribute("areas", areas);
	            model.addAttribute("cargos", cargos);
	            model.addAttribute("usuarioActual", empleadoActual.getFkUsuario());
	            model.addAttribute("firmaActual", empleadoActual.getFkFirmaElectronica());
	            model.addAttribute("esEdicion", true);
	            return "empleado/editarempleado";
	        } catch (Exception e) {
	            return "error";
	        }
	    }
	    
	    @PostMapping("/actualizar/{id}")
	    public String actualizarEmpleado(
	    		@PathVariable int id,
	    		@ModelAttribute EmpleadoRequestDto empleado,
	    		@ModelAttribute("usuarioContrasenia") String usuarioContrasenia,
	    		HttpSession session,
	    		Model model) {
	    	empleado.setIdEmpleado(id);
	    	try {
	    		// Las fechas se revisan primero: si el registro se va a rechazar, no
	    		// tiene sentido haber cambiado ya la contrasena del usuario.
	    		String avisoFechas = errorEnFechasDelEmpleado(empleado);
	    		if (avisoFechas != null) {
	    			return mostrarAviso(model, empleado, avisoFechas);
	    		}

	    		// La recuperacion de contrasenia es exclusiva del Gerente General.
	    		// Aunque alguien manipule el formulario, aqui se descarta el valor.
	    		if (!esGerenteGeneral(session)) {
	    			usuarioContrasenia = null;
	    		}

	    		// If a new password was typed during edit, update the linked Usuario too
	    		if (usuarioContrasenia != null && !usuarioContrasenia.isBlank() && empleado.getFkUsuario() > 0) {
	    			UsuarioResponseDto usuarioActual = usuarioService.listarUsuarios().stream()
	    					.filter(u -> u.getIdUsuario() == empleado.getFkUsuario())
	    					.findFirst()
	    					.orElse(null);
	    			if (usuarioActual != null) {
	    				UsuarioRequestDto usuarioUpdate = new UsuarioRequestDto();
	    				usuarioUpdate.setIdUsuario(usuarioActual.getIdUsuario());
	    				usuarioUpdate.setNombre(usuarioActual.getNombre());
	    				usuarioUpdate.setCorreo(usuarioActual.getCorreo());
	    				usuarioUpdate.setContrasenia(usuarioContrasenia);
	    				usuarioUpdate.setFechaCreacion(usuarioActual.getFechaCreacion());
	    				usuarioUpdate.setEstadoUsuario(usuarioActual.isEstadoUsuario());
	    				usuarioService.guardarUsuarios(usuarioUpdate);
	    			}
	    		}

	    		if (cedulaYaRegistrada(empleado.getCi(), id)) {
	    			return mostrarCedulaDuplicada(model, empleado);
	    		}

	    		empleadoService.guardarEmpleados(empleado);
	    		return "redirect:/empleado/listar?success=true";

	    	} catch (WebClientResponseException.Conflict ex) {
	    		return mostrarCedulaDuplicada(model, empleado);
	    	} catch (Exception e) {
	    		return mostrarErrorEdicion(model, empleado, e);
	    	}
	    }

	    private String mostrarErrorEdicion(Model model, EmpleadoRequestDto empleado, Exception e) {
	    	List<AreaResponseDto> areas = areaService.listarAreas();
	    	List<CargoResponseDto> cargos = cargoService.listarCargos();

	    	model.addAttribute("empleado", empleado);
	    	model.addAttribute("areas", areas);
	    	model.addAttribute("cargos", cargos);
	    	model.addAttribute("esEdicion", true);
	    	model.addAttribute("error", "No se pudo guardar el empleado: " + e.getMessage());
	    	return "empleado/editarempleado";
	    }
	    
	    @GetMapping("/eliminar/{id}")
	    public String eliminarEmpleado(@PathVariable int id) {
	        try {
	            empleadoService.eliminarEmpleado(id);
	            return "redirect:/empleado/listar?deleted=true";
	        } catch (Exception e) {
	            return "redirect:/empleado/listar?error=true";
	        }
	    }

	    private String mostrarErrorDuplicado(Model model, EmpleadoRequestDto empleado, WebClientResponseException ex) {
	    	return mostrarAviso(model, empleado,
	    			"Ya existe un empleado registrado con ese número de cédula");
	    }

	    /**
	     * Repinta el formulario con el aviso de cedula repetida, conservando lo que
	     * el usuario ya habia escrito.
	     */
	    private String mostrarCedulaDuplicada(Model model, EmpleadoRequestDto empleado) {
	    	return mostrarAviso(model, empleado,
	    			"Ya existe un empleado registrado con ese número de cédula");
	    }

	    /**
	     * Repinta el formulario con un aviso amarillo, conservando lo que el usuario
	     * ya habia escrito. Es la unica forma de devolver el control sin que pierda
	     * los datos que tecleo.
	     */
	    private String mostrarAviso(Model model, EmpleadoRequestDto empleado, String mensaje) {
	    	model.addAttribute("empleado", empleado);
	    	model.addAttribute("areas", areaService.listarAreas());
	    	model.addAttribute("cargos", cargoService.listarCargos());
	    	model.addAttribute("roles", rolService.listarRoles());
	    	model.addAttribute("mensajeAviso", mensaje);
	    	model.addAttribute("esEdicion", empleado.getIdEmpleado() > 0);
	    	return empleado.getIdEmpleado() > 0 ? "empleado/editarempleado" : "empleado/nuevoempleado";
	    }

	    /**
	     * Un empleado no puede salir antes de haber entrado.
	     * Devuelve null si las fechas son correctas, o el mensaje a mostrar si no.
	     *
	     * La fecha de salida vacia es valida: significa que sigue trabajando.
	     */
	    private String errorEnFechasDelEmpleado(EmpleadoRequestDto empleado) {
	    	if (empleado.getFechaSalida() == null) {
	    		return null;
	    	}
	    	if (empleado.getFechaIngreso() == null) {
	    		return "No se puede registrar una fecha de salida sin fecha de ingreso.";
	    	}

	    	LocalDate ingreso = aDia(empleado.getFechaIngreso());
	    	LocalDate salida = aDia(empleado.getFechaSalida());

	    	if (salida.isBefore(ingreso)) {
	    		return "La fecha de salida (" + comoTexto(salida) + ") no puede ser anterior "
	    				+ "a la fecha de ingreso (" + comoTexto(ingreso) + ").";
	    	}
	    	return null;
	    }

	    /**
	     * Una firma electronica no puede expirar antes de haber sido creada.
	     * Mismo defecto logico que el de ingreso/salida, dos campos mas abajo.
	     */
	    private String errorEnFechasDeLaFirma(Date creacion, Date expiracion) {
	    	if (creacion == null || expiracion == null) {
	    		return null;
	    	}
	    	if (aDia(expiracion).isBefore(aDia(creacion))) {
	    		return "La fecha de expiración de la firma no puede ser anterior "
	    				+ "a su fecha de creación.";
	    	}
	    	return null;
	    }

	    /**
	     * Reduce una fecha a su dia calendario.
	     *
	     * Es imprescindible: la fecha de ingreso se asigna con new Date() y trae
	     * hora, mientras que la de salida llega a medianoche desde un input
	     * type="date". Comparando instantes, un empleado que entra y sale el mismo
	     * dia seria rechazado por error.
	     *
	     * Se usa Instant.ofEpochMilli y no fecha.toInstant() porque si llegara un
	     * java.sql.Date, toInstant() lanza UnsupportedOperationException.
	     */
	    private LocalDate aDia(Date fecha) {
	    	return Instant.ofEpochMilli(fecha.getTime())
	    			.atZone(ZoneId.systemDefault())
	    			.toLocalDate();
	    }

	    private String comoTexto(LocalDate fecha) {
	    	return new SimpleDateFormat("dd/MM/yyyy").format(
	    			Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	    }

	    /**
	     * Comprueba si la cedula ya pertenece a otro empleado. Se compara sin
	     * espacios sobrantes, igual que en el backend.
	     */
	    private boolean cedulaYaRegistrada(String cedula, int idPropio) {
	    	if (cedula == null || cedula.isBlank()) {
	    		return false;
	    	}
	    	String limpia = cedula.trim();
	    	return empleadoService.listarEmpleados().stream()
	    			.filter(e -> e.getIdEmpleado() != idPropio)
	    			.anyMatch(e -> e.getCi() != null && e.getCi().trim().equalsIgnoreCase(limpia));
	    }
	    

	    /**
	     * Indica si quien tiene la sesion abierta es Gerente General. Se usa para
	     * habilitar el cambio de contrasenia, que es exclusivo de ese rol.
	     */
	    private boolean esGerenteGeneral(HttpSession session) {
	        Object attr = session != null ? session.getAttribute("usuarioActual") : null;
	        if (!(attr instanceof LoginResponseDto usuario) || usuario.getRol() == null) {
	            return false;
	        }
	        return "Gerente General".equalsIgnoreCase(usuario.getRol().trim());
	    }
}