package com.uisrael.prototipogestalabweb.controller;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uisrael.prototipogestalabweb.model.dto.request.ClienteCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.CotizacionCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.DetalleCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CatalogoNormServiCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CatalogoParametroCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CatalogoTerminoCondiCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.ClienteCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CotizacionCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DescripcionServicioCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DetalleCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EmpleadoResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.LmpCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.LoginResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.PlazoEntregaCResponseDto;
import com.uisrael.prototipogestalabweb.services.ICatalogoNormServiCService;
import com.uisrael.prototipogestalabweb.services.ICatalogoParametroCService;
import com.uisrael.prototipogestalabweb.services.ICatalogoTerminoCondiCService;
import com.uisrael.prototipogestalabweb.services.IClienteCService;
import com.uisrael.prototipogestalabweb.services.ICotizacionCService;
import com.uisrael.prototipogestalabweb.services.IDescripcionServicioCService;
import com.uisrael.prototipogestalabweb.services.IDetalleCService;
import com.uisrael.prototipogestalabweb.services.IEmpleadoService;
import com.uisrael.prototipogestalabweb.services.ILmpCService;
import com.uisrael.prototipogestalabweb.services.IPlazoEntregaCService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cotizacion")
public class CotizacionController {
	
	private final ICotizacionCService cotizacionService;
	private final IDetalleCService detalleService;
	private final IClienteCService clienteService;
	private final ICatalogoTerminoCondiCService terminoService;
	private final IEmpleadoService empleadoService;
	private final ICatalogoParametroCService parametroService;
	private final ICatalogoNormServiCService normaService;
	private final ILmpCService lmpService;
	private final IDescripcionServicioCService descripcionServicioService;
	private final IPlazoEntregaCService plazoEntregaService;
	
	public CotizacionController(ICotizacionCService cotizacionService, IDetalleCService detalleService,
			IClienteCService clienteService, ICatalogoTerminoCondiCService terminoService,
			IEmpleadoService empleadoService, ICatalogoParametroCService parametroService,
			ICatalogoNormServiCService normaService, ILmpCService lmpService,
			IDescripcionServicioCService descripcionServicioService, IPlazoEntregaCService plazoEntregaService) {
		super();
		this.cotizacionService = cotizacionService;
		this.detalleService = detalleService;
		this.clienteService = clienteService;
		this.terminoService = terminoService;
		this.empleadoService = empleadoService;
		this.parametroService = parametroService;
		this.normaService = normaService;
		this.lmpService = lmpService;
		this.descripcionServicioService = descripcionServicioService;
		this.plazoEntregaService = plazoEntregaService;
	}

	@GetMapping("/listar")
	public String listarCotizaciones(Model model) {
		List<CotizacionCResponseDto> cotizaciones = cotizacionService.listarCotizaciones();
		model.addAttribute("cotizaciones", cotizaciones);
		return "cotizacion/listarcotizacion";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model, HttpSession session) {
		cargarListasDeApoyo(model);
		CotizacionCRequestDto cotizacion = new CotizacionCRequestDto();
		// La fecha de elaboracion es siempre el dia en que se genera la cotizacion.
		cotizacion.setFechaElaboracionCotizacionC(new Date());

		Object usuarioObj = session.getAttribute("usuarioActual");
		if (usuarioObj instanceof LoginResponseDto usuarioActual) {
			EmpleadoResponseDto empleadoActual = empleadoService.listarEmpleados().stream()
					.filter(e -> e.getFkUsuario() != null && e.getFkUsuario().getIdUsuario() == usuarioActual.getIdUsuario())
					.findFirst()
					.orElse(null);
			if (empleadoActual != null) {
				cotizacion.setFkEmpleado(empleadoActual.getIdEmpleado());
				model.addAttribute("empleadoActual", empleadoActual);
			}
		}

		model.addAttribute("cotizacion", cotizacion);
		return "cotizacion/nuevacotizacion";
	}

	@PostMapping("/guardar")
	public String guardarCotizacion(
			@ModelAttribute CotizacionCRequestDto cotizacion,
			@RequestParam(required = false) String clienteNuevoTipo,
			@RequestParam(required = false) String clienteNuevoCi,
			@RequestParam(required = false) String clienteNuevoNombre,
			@RequestParam(required = false) String clienteNuevoContacto,
			@RequestParam(required = false) String clienteNuevoDireccion,
			@RequestParam(required = false) String clienteNuevoTelefono,
			@RequestParam(required = false) String clienteNuevoCorreo,
			@RequestParam(required = false) List<Integer> detalleParametro,
			@RequestParam(required = false) List<Integer> detalleLmp,
			@RequestParam(required = false) List<Integer> detalleDescripcionServicio,
			@RequestParam(required = false) List<Integer> detallePlazoEntrega,
			@RequestParam(required = false) List<Integer> detalleCantidadPuntos,
			@RequestParam(required = false) List<Double> detallePrecioUnitario,
			@RequestParam(required = false) List<String> detalleCondicion) {

		// Se fija en el servidor, no se toma del formulario: el campo de la vista
		// es de solo lectura y el dato no debe depender de lo que llegue.
		cotizacion.setFechaElaboracionCotizacionC(new Date());

		if (cotizacion.getFkCliente() == 0 && clienteNuevoNombre != null && !clienteNuevoNombre.isBlank()) {
			ClienteCRequestDto nuevoCliente = new ClienteCRequestDto();
			nuevoCliente.setTipoClienteC(clienteNuevoTipo);
			nuevoCliente.setCiClienteC(clienteNuevoCi);
			nuevoCliente.setNombreRazonSocialClienteC(clienteNuevoNombre);
			nuevoCliente.setNombrePersonaContactoClienteC(clienteNuevoContacto);
			nuevoCliente.setDireccionClienteC(clienteNuevoDireccion);
			nuevoCliente.setTelefonoClienteC(clienteNuevoTelefono);
			nuevoCliente.setCorreoClienteC(clienteNuevoCorreo);
			nuevoCliente.setEstadoClienteC(true);
			ClienteCResponseDto clienteCreado = clienteService.guardarCliente(nuevoCliente);
			cotizacion.setFkCliente(clienteCreado.getIdClienteC());
		}

		CotizacionCResponseDto guardada = cotizacionService.guardarCotizacion(cotizacion);

		if (detalleParametro != null) {
			for (int i = 0; i < detalleParametro.size(); i++) {
				DetalleCRequestDto detalle = new DetalleCRequestDto();
				detalle.setFkCotizacion(guardada.getIdCotizacionC());
				detalle.setFkParametro(detalleParametro.get(i));
				if (detalleLmp != null && i < detalleLmp.size()) {
					detalle.setFkLmp(detalleLmp.get(i));
				}
				if (detalleDescripcionServicio != null && i < detalleDescripcionServicio.size()) {
					detalle.setFkDescripcionServicio(detalleDescripcionServicio.get(i));
				}
				detalle.setFkPlazoEntrega(detallePlazoEntrega.get(i));
				detalle.setCantidadPuntosDetalleC(detalleCantidadPuntos.get(i));
				detalle.setPrecioUnitarioDetalleC(detallePrecioUnitario.get(i));
				detalle.setCondicionDetalleC(detalleCondicion.get(i));
				detalle.setPrecioTotalDetalleC(detallePrecioUnitario.get(i) * detalleCantidadPuntos.get(i));
				detalleService.guardarDetalle(detalle);
			}
		}

		// Tras crearla se abre la pantalla de edicion, que es donde ahora se
		// gestionan las lineas de detalle.
		return "redirect:/cotizacion/editar/" + guardada.getIdCotizacionC() + "?success=true";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		try {

			if (estaAprobada(id)) {
				return "redirect:/cotizacion/listar?bloqueada=true";
			}

			CotizacionCResponseDto actual = cotizacionService.buscarPorId(id);

			CotizacionCRequestDto form = new CotizacionCRequestDto();
			form.setIdCotizacionC(actual.getIdCotizacionC());
			form.setFechaElaboracionCotizacionC(actual.getFechaElaboracionCotizacionC());
			form.setVigenciaDiasCotizacionC(actual.getVigenciaDiasCotizacionC());
			form.setElaboradoPorCotizacionC(actual.getElaboradoPorCotizacionC());
			// Sin esta linea el estado llegaba siempre en false y editar la
			// cotizacion la dejaba Inactiva sin que nadie lo pidiera.
			form.setEstadoCotizacionC(actual.isEstadoCotizacionC());
			form.setSubtotalAgua(actual.getSubtotalAgua());
			form.setSubtotalRuido(actual.getSubtotalRuido());
			form.setSubtotalEmiciones(actual.getSubtotalEmiciones());
			form.setSubtotalCalidad(actual.getSubtotalCalidad());
			form.setSubtotalSuelo(actual.getSubtotalSuelo());
			form.setCostoLogistica(actual.getCostoLogistica());
			form.setIva(actual.getIva());
			form.setTotalCotizacionC(actual.getTotalCotizacionC());
			if (actual.getFkCliente() != null) {
				form.setFkCliente(actual.getFkCliente().getIdClienteC());
				model.addAttribute("nombreCliente", actual.getFkCliente().getNombreRazonSocialClienteC());
			}
			if (actual.getFkEmpleado() != null) {
				form.setFkEmpleado(actual.getFkEmpleado().getIdEmpleado());
				model.addAttribute("nombreElaborador",
						actual.getFkEmpleado().getNombre() + " " + actual.getFkEmpleado().getApellido());
			}
			if (actual.getFkNormaServicio() != null) {
				form.setFkNormaServicio(actual.getFkNormaServicio().getIdCatalogoNormServi());
			}
			if (actual.getFkLmp() != null) {
				form.setFkLmp(actual.getFkLmp().getIdLmpC());
			}

			cargarListasDeApoyo(model);
			// Las lineas de detalle se gestionan desde esta misma pantalla.
			model.addAttribute("detalles", detalleService.listarPorCotizacion(id));
			model.addAttribute("cotizacion", form);
			model.addAttribute("esEdicion", true);
			return "cotizacion/editarcotizacion";
		}
		catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/actualizar/{id}")
	public String actualizarCotizacion(@PathVariable int id, @ModelAttribute CotizacionCRequestDto cotizacion) {
		cotizacion.setIdCotizacionC(id);
		try {

			if (estaAprobada(id)) {
				return "redirect:/cotizacion/listar?bloqueada=true";
			}

			// Los datos que la pantalla ya no permite editar se vuelven a leer de
			// la base y no del formulario: aunque alguien manipule el HTML del
			// navegador, cliente, empleado, fecha y estado no cambian.
			CotizacionCResponseDto original = cotizacionService.buscarPorId(id);
			if (original != null) {
				cotizacion.setFechaElaboracionCotizacionC(original.getFechaElaboracionCotizacionC());
				cotizacion.setEstadoCotizacionC(original.isEstadoCotizacionC());
				cotizacion.setElaboradoPorCotizacionC(original.getElaboradoPorCotizacionC());
				if (original.getFkCliente() != null) {
					cotizacion.setFkCliente(original.getFkCliente().getIdClienteC());
				}
				if (original.getFkEmpleado() != null) {
					cotizacion.setFkEmpleado(original.getFkEmpleado().getIdEmpleado());
				}
			}

			cotizacionService.guardarCotizacion(cotizacion);
			return "redirect:/cotizacion/editar/" + id + "?success=true";
		} catch (Exception e) {
			return "redirect:/cotizacion/editar/" + id + "?error=true";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarCotizacion(@PathVariable int id) {
		try {
			cotizacionService.eliminarCotizacion(id);
			return "redirect:/cotizacion/listar?deleted=true";
		} catch (Exception e) {
			return "redirect:/cotizacion/listar?error=true";
		}
	}

	@GetMapping("/detalle/{id}")
	public String verDetalle(@PathVariable int id, Model model) {
		try {
			CotizacionCResponseDto cotizacion = cotizacionService.buscarPorId(id);
			List<DetalleCResponseDto> detallesDeEstaCotizacion = detalleService.listarPorCotizacion(id);
			List<CatalogoTerminoCondiCResponseDto> todosLosTerminos = terminoService.listarTerminos();

			model.addAttribute("todosLosTerminos", todosLosTerminos);
			model.addAttribute("cotizacion", cotizacion);
			model.addAttribute("detalles", detallesDeEstaCotizacion);

			return "cotizacion/detallecotizacion";
		} catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/detalle/guardar/{idCotizacion}")
	public String guardarDetalle(@PathVariable int idCotizacion, @ModelAttribute DetalleCRequestDto detalle) {
		if (estaAprobada(idCotizacion)) {
			return "redirect:/cotizacion/editar/" + idCotizacion + "?bloqueada=true";
		}
		detalle.setFkCotizacion(idCotizacion);
		detalle.setPrecioTotalDetalleC(detalle.getPrecioUnitarioDetalleC() * detalle.getCantidadPuntosDetalleC());
		detalleService.guardarDetalle(detalle);
		return "redirect:/cotizacion/editar/" + idCotizacion + "?success=true";
	}

	@GetMapping("/detalle/eliminar/{idDetalle}/{idCotizacion}")
	public String eliminarDetalle(@PathVariable int idDetalle, @PathVariable int idCotizacion) {
		try {

			if (estaAprobada(idCotizacion)) {
				return "redirect:/cotizacion/editar/" + idCotizacion + "?bloqueada=true";
			}

			detalleService.eliminarDetalle(idDetalle);
			return "redirect:/cotizacion/editar/" + idCotizacion + "?deleted=true";
		} catch (Exception e) {
			return "redirect:/cotizacion/editar/" + idCotizacion + "?error=true";
		}
	}

	private void cargarListasDeApoyo(Model model) {
		List<ClienteCResponseDto> clientes = clienteService.listarClientes();
		List<CatalogoTerminoCondiCResponseDto> terminos = terminoService.listarTerminos();
		List<EmpleadoResponseDto> empleados = empleadoService.listarEmpleados();
		List<CatalogoParametroCResponseDto> parametros = parametroService.listarParametros();
		List<CatalogoNormServiCResponseDto> normas = normaService.listarNormas();
		List<LmpCResponseDto> lmps = lmpService.listarLmpCs();
		List<DescripcionServicioCResponseDto> descripciones = descripcionServicioService.listarDescripcionServicioCs();
		List<PlazoEntregaCResponseDto> plazos = plazoEntregaService.listarPlazoEntregaCs();
		model.addAttribute("clientes", clientes);
		model.addAttribute("terminos", terminos);
		model.addAttribute("empleados", empleados);
		model.addAttribute("parametros", parametros);
		model.addAttribute("normas", normas);
		model.addAttribute("lmps", lmps);
		model.addAttribute("descripciones", descripciones);
		model.addAttribute("plazos", plazos);

	}

	private boolean estaAprobada(int idCotizacion) {
		try {
			CotizacionCResponseDto cotizacion = cotizacionService.buscarPorId(idCotizacion);
			return cotizacion != null && "APROBADA".equalsIgnoreCase(cotizacion.getEstadoAprobacion());
		} catch (Exception e) {
			return false;
		}
	}
}
