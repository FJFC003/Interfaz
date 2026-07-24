package com.uisrael.prototipogestalabweb.controller;

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

	// Display list of employees
    @GetMapping("/listar")
    public String listarEmpleados(Model model) {
            List<EmpleadoResponseDto> empleadosBD = empleadoService.listarEmpleados();
            model.addAttribute("empleados", empleadosBD);
            return "empleado/listarempleado";
    }

    // Show create employee form
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
            List<AreaResponseDto> areas = areaService.listarAreas();
            List<CargoResponseDto> cargos = cargoService.listarCargos();
            List<RolResponseDto> roles = rolService.listarRoles();
            
            model.addAttribute("empleado", new EmpleadoRequestDto());
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
    				Model model) {

    	try {
    		// 1. Create the user account
    		UsuarioRequestDto nuevoUsuario = new UsuarioRequestDto();
    		nuevoUsuario.setNombre(usuarioNombre);
    		nuevoUsuario.setCorreo(empleado.getCorreo());
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
    		Model model) {
    	empleado.setIdEmpleado(id);
    	try {
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

    		empleadoService.guardarEmpleados(empleado);
    		return "redirect:/empleado/listar?success=true";

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
    	List<AreaResponseDto> areas = areaService.listarAreas();
    	List<CargoResponseDto> cargos = cargoService.listarCargos();
    	List<RolResponseDto> roles = rolService.listarRoles();

    	model.addAttribute("empleado", empleado);
    	model.addAttribute("areas", areas);
    	model.addAttribute("cargos", cargos);
    	model.addAttribute("roles", roles);
    	model.addAttribute("error", "Ya existe un empleado registrado con esa cédula.");
    	return empleado.getIdEmpleado() > 0 ? "empleado/editarempleado" : "empleado/nuevoempleado";
    }
    
}
