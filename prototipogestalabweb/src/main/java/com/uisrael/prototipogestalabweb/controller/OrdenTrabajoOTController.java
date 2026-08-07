package com.uisrael.prototipogestalabweb.controller;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import com.uisrael.prototipogestalabweb.model.dto.request.DesviosOrdenOTRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.DetalleOrdenTrabajoOTRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.OrdenTrabajoOTRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CotizacionCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DetalleCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.OrdenTrabajoOTResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.ParametroAnalizarPLResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.PlanMuestreoPLResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.RecursosCronoPLResponseDto;
import com.uisrael.prototipogestalabweb.services.IDesviosOrdenOTService;
import com.uisrael.prototipogestalabweb.services.IDetalleOrdenTrabajoOTService;
import com.uisrael.prototipogestalabweb.services.IEmpleadoService;
import com.uisrael.prototipogestalabweb.services.IOrdenTrabajoOTService;
import com.uisrael.prototipogestalabweb.services.IParametroAnalizarPLService;
import com.uisrael.prototipogestalabweb.services.IPlanMuestreoPLService;
import com.uisrael.prototipogestalabweb.services.IRecursosCronoPLService;


@Controller
@RequestMapping("/ordentrabajo")
public class OrdenTrabajoOTController {

	private final IOrdenTrabajoOTService ordenService;
	private final IDetalleOrdenTrabajoOTService monitoreoService;
	private final IDesviosOrdenOTService desvioService;
	private final IPlanMuestreoPLService planService;
	private final IRecursosCronoPLService recursosService;
	private final IParametroAnalizarPLService parametroService;
	private final IEmpleadoService empleadoService;
	private final SpringTemplateEngine templateEngine;

	public OrdenTrabajoOTController(IOrdenTrabajoOTService ordenService, IDetalleOrdenTrabajoOTService monitoreoService,
			IDesviosOrdenOTService desvioService, IPlanMuestreoPLService planService,
			IRecursosCronoPLService recursosService, IParametroAnalizarPLService parametroService,
			IEmpleadoService empleadoService, SpringTemplateEngine templateEngine) {
		super();
		this.ordenService = ordenService;
		this.monitoreoService = monitoreoService;
		this.desvioService = desvioService;
		this.planService = planService;
		this.recursosService = recursosService;
		this.parametroService = parametroService;
		this.empleadoService = empleadoService;
		this.templateEngine = templateEngine;
	}

	@GetMapping("/listar")
	public String listarOrdenes(Model model) {
		model.addAttribute("ordenes", ordenService.listarOrdenes());
		return "ordentrabajo/listarorden";
	}

	@GetMapping("/pendientes")
	public String planesCompletados(Model model) {
		List<PlanMuestreoPLResponseDto> listos = new ArrayList<>();
		List<PlanMuestreoPLResponseDto> enProceso = new ArrayList<>();

		for (PlanMuestreoPLResponseDto p : planService.listarPlanes()) {
			if ("COMPLETADO".equalsIgnoreCase(p.getEstadoPlan())) {
				listos.add(p);
			} else if ("ENVIADO".equalsIgnoreCase(p.getEstadoPlan())) {
				enProceso.add(p);
			}
		}

		model.addAttribute("planes", listos);
		model.addAttribute("enProceso", enProceso);
		// Planes que ya tienen su Orden de Trabajo emitida: la pantalla usa este
		// mapa para cambiar el boton "Generar OT" por el aviso correspondiente.
		model.addAttribute("otPorPlan", ordenesPorPlan());
		return "ordentrabajo/pendientesorden";
	}

	/**
	 * Ordenes ya emitidas, indexadas por el plan del que salieron.
	 * Se recorre la lista una sola vez en lugar de preguntar plan por plan.
	 */
	private java.util.Map<Integer, OrdenTrabajoOTResponseDto> ordenesPorPlan() {
		java.util.Map<Integer, OrdenTrabajoOTResponseDto> mapa = new java.util.HashMap<>();
		try {
			for (OrdenTrabajoOTResponseDto ot : ordenService.listarOrdenes()) {
				if (ot.getFkPlanMuestreo() == null) {
					continue;
				}
				// Si por datos antiguos hubiera dos ordenes del mismo plan se
				// conserva la primera, que es la que abre el control de duplicados.
				mapa.putIfAbsent(ot.getFkPlanMuestreo().getIdPlan(), ot);
			}
		} catch (Exception e) {
			return mapa;
		}
		return mapa;
	}

	/**
	 * La orden solo se edita mientras esta en ejecucion. Una vez enviada al
	 * Laboratorio queda cerrada hasta que alguien la devuelva a Coordinacion.
	 */
	private boolean puedeEditarOrden(int idOT) {
		try {
			OrdenTrabajoOTResponseDto orden = ordenService.buscarPorId(idOT);
			return orden == null || orden.getEstadoOT() == null
					|| "EN_EJECUCION".equalsIgnoreCase(orden.getEstadoOT());
		} catch (Exception e) {
			return false;
		}
	}

	// ================= GENERAR OT DESDE EL PLAN =================

	/**
	 * Arma la Orden de Trabajo con todo lo que el sistema ya sabe:
	 * cotizacion, responsable, cliente, la linea de monitoreo y los desvios
	 * (los parametros que el Tecnico marco como "In situ").
	 * La Coordinacion Tecnica solo revisa y confirma.
	 */
	@GetMapping("/nuevo/{idPlan}")
	public String mostrarFormularioNuevo(@PathVariable int idPlan, Model model) {
		try {
			// Un plan tiene una sola Orden de Trabajo. La pantalla ya no ofrece
			// el boton, pero la direccion se puede escribir a mano.
			List<OrdenTrabajoOTResponseDto> yaEmitidas = ordenService.listarPorPlan(idPlan);
			if (yaEmitidas != null && !yaEmitidas.isEmpty()) {
				return "redirect:/ordentrabajo/detalle/" + yaEmitidas.get(0).getIdOT() + "?duplicada=true";
			}

			PlanMuestreoPLResponseDto plan = planService.buscarPorId(idPlan);

			OrdenTrabajoOTRequestDto form = new OrdenTrabajoOTRequestDto();
			form.setFkPlanMuestreo(idPlan);
			form.setNoItemOT(1);

			// Fecha de emision <- fecha de elaboracion del plan
			form.setFechaEmisionOT(plan.getFechaElaboracion());

			// Responsable <- responsable del plan de muestreo
			if (plan.getFkResponsable() != null) {
				form.setFkResponsableEmision(plan.getFkResponsable().getIdEmpleado());
			}

			// Persona de contacto <- cliente (razon social o persona natural)
			CotizacionCResponseDto cotizacion = null;
			DetalleCResponseDto detalle = plan.getFkDetalleCotizacion();
			if (detalle != null) {
				cotizacion = detalle.getFkCotizacion();
			}
			if (cotizacion != null && cotizacion.getFkCliente() != null) {
				form.setNombrePersonaContactoOT(cotizacion.getFkCliente().getNombreRazonSocialClienteC());
			}

			// Tecnico asignado <- el que hizo el muestreo
			List<RecursosCronoPLResponseDto> recursos = recursosService.listarPorPlan(idPlan);
			if (!recursos.isEmpty() && recursos.get(0).getFkTecnico() != null) {
				form.setFkTecnicoAsignado(recursos.get(0).getFkTecnico().getIdEmpleado());
			}

			model.addAttribute("orden", form);
			model.addAttribute("plan", plan);
			model.addAttribute("cotizacion", cotizacion);
			model.addAttribute("detalle", detalle);
			model.addAttribute("recursos", recursos);
			model.addAttribute("parametros", parametroService.listarPorPlan(idPlan));
			model.addAttribute("empleados", empleadoService.listarEmpleados());
			return "ordentrabajo/nuevaorden";

		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	@PostMapping("/guardar")
	public String guardarOrden(@ModelAttribute OrdenTrabajoOTRequestDto orden, Model model) {
		try {
			// Guarda contra el doble envio: si el plan ya tiene una orden emitida,
			// no se crea otra; se abre la que ya existe.
			List<OrdenTrabajoOTResponseDto> yaEmitidas = ordenService.listarPorPlan(orden.getFkPlanMuestreo());
			if (yaEmitidas != null && !yaEmitidas.isEmpty()) {
				return "redirect:/ordentrabajo/detalle/" + yaEmitidas.get(0).getIdOT() + "?duplicada=true";
			}

			OrdenTrabajoOTResponseDto creada = ordenService.guardar(orden);
			generarLineasDesdeElPlan(creada.getIdOT(), orden.getFkPlanMuestreo());
			return "redirect:/ordentrabajo/detalle/" + creada.getIdOT() + "?success=true";

		} catch (WebClientResponseException ex) {
			model.addAttribute("mensajeError", ex.getResponseBodyAsString());
			return "error";
		} catch (Exception ex) {
			model.addAttribute("mensajeError", ex.getClass().getSimpleName() + " - " + ex.getMessage());
			return "error";
		}
	}

	private void generarLineasDesdeElPlan(int idOT, int idPlan) {
		try {
			PlanMuestreoPLResponseDto plan = planService.buscarPorId(idPlan);
			DetalleCResponseDto detalle = plan.getFkDetalleCotizacion();

			// ---- MONITOREOS ----
			StringBuilder actividad = new StringBuilder();
			if (detalle != null && detalle.getFkDescripcionServicio() != null) {
				actividad.append(detalle.getFkDescripcionServicio().getTextoDescripcionServicioC());
			}
			if (plan.getObjetivoPlan() != null && !plan.getObjetivoPlan().isBlank()) {
				if (actividad.length() > 0) {
					actividad.append(". ");
				}
				actividad.append(plan.getObjetivoPlan());
			}
			if (actividad.length() == 0) {
				actividad.append("Muestreo y análisis según plan ").append(plan.getCodigoPlan());
			}

			DetalleOrdenTrabajoOTRequestDto monitoreo = new DetalleOrdenTrabajoOTRequestDto();
			monitoreo.setNoItemDetalleOrdenOT(1);
			monitoreo.setDescripcionActividadDetalleOrdenOT(actividad.toString());

			List<RecursosCronoPLResponseDto> recursos = recursosService.listarPorPlan(idPlan);
			if (!recursos.isEmpty()) {
				monitoreo.setFechaPlanificadaDetalleOrdenOT(recursos.get(0).getFechaMuestreo());
			} else {
				monitoreo.setFechaPlanificadaDetalleOrdenOT(plan.getFechaElaboracion());
			}

			int puntos = (detalle != null && detalle.getCantidadPuntosDetalleC() > 0)
					? detalle.getCantidadPuntosDetalleC() : 1;
			monitoreo.setPuntosPlanificadosDetalleOrdenOT(puntos);
			monitoreo.setPuntosEjecutadosDetalleOrdenOT(0);   // lo llena la Coordinacion Tecnica
			monitoreo.setFkOrdenTrabajo(idOT);
			monitoreoService.guardar(monitoreo);

			// ---- DESVIOS: los parametros medidos In situ ----
			int item = 1;
			for (ParametroAnalizarPLResponseDto p : parametroService.listarPorPlan(idPlan)) {
				if (!"In situ".equalsIgnoreCase(p.getSitioMedicion())) {
					continue;
				}

				StringBuilder desc = new StringBuilder();
				desc.append("Parámetro medido In situ: ").append(p.getParametros());
				if (p.getUnidadMedida() != null && !p.getUnidadMedida().isBlank()) {
					desc.append(" (").append(p.getUnidadMedida()).append(")");
				}
				desc.append(". Sitio de medición: ").append(p.getSitioMedicion());
				if (p.getPreservacion() != null && !p.getPreservacion().isBlank()) {
					desc.append(". Preservación: ").append(p.getPreservacion());
				}

				DesviosOrdenOTRequestDto desvio = new DesviosOrdenOTRequestDto();
				desvio.setNoItemDesviosOrdenOT(item++);
				desvio.setDescripcionDesviosOrdenOT(desc.toString());
				desvio.setPuntosModificadosDesviosOrdenOT(0);
				desvio.setFkOrdenTrabajo(idOT);
				desvioService.guardar(desvio);
			}

		} catch (Exception e) {
			// La generacion es una ayuda: si falla, la orden ya quedo creada.
			e.printStackTrace();
		}
	}

	// ================= DETALLE =================

	@GetMapping("/detalle/{idOT}")
	public String verDetalle(@PathVariable int idOT, Model model) {
		try {
			model.addAttribute("orden", ordenService.buscarPorId(idOT));
			model.addAttribute("monitoreos", monitoreoService.listarPorOrden(idOT));
			model.addAttribute("desvios", desvioService.listarPorOrden(idOT));
			model.addAttribute("nuevoMonitoreo", new DetalleOrdenTrabajoOTRequestDto());
			model.addAttribute("nuevoDesvio", new DesviosOrdenOTRequestDto());
			// Enviada al Laboratorio o con informe emitido = solo lectura.
			model.addAttribute("puedeEditar", puedeEditarOrden(idOT));
			return "ordentrabajo/detalleorden";
		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	// ================= ENVIO AL LABORATORIO =================

	@PostMapping("/enviar-laboratorio/{idOT}")
	public String enviarALaboratorio(@PathVariable int idOT, Model model) {
		try {
			ordenService.enviarALaboratorio(idOT);
			return "redirect:/ordentrabajo/detalle/" + idOT + "?enviada=true";
		} catch (WebClientResponseException ex) {
			model.addAttribute("mensajeError", ex.getResponseBodyAsString());
			return "error";
		} catch (Exception ex) {
			model.addAttribute("mensajeError", ex.getMessage());
			return "error";
		}
	}

	@PostMapping("/devolver/{idOT}")
	public String devolverACoordinacion(@PathVariable int idOT, Model model) {
		try {
			ordenService.devolverACoordinacion(idOT);
			return "redirect:/ordentrabajo/detalle/" + idOT + "?devuelta=true";
		} catch (WebClientResponseException ex) {
			model.addAttribute("mensajeError", ex.getResponseBodyAsString());
			return "error";
		} catch (Exception ex) {
			model.addAttribute("mensajeError", ex.getMessage());
			return "error";
		}
	}

	@GetMapping("/eliminar/{idOT}")
	public String eliminarOrden(@PathVariable int idOT) {
		try {
			ordenService.eliminar(idOT);
			return "redirect:/ordentrabajo/listar?deleted=true";
		} catch (Exception e) {
			return "redirect:/ordentrabajo/listar?error=true";
		}
	}

	// ================= LINEAS DE LA ORDEN =================

	@PostMapping("/monitoreo/guardar/{idOT}")
	public String guardarMonitoreo(@PathVariable int idOT, @ModelAttribute DetalleOrdenTrabajoOTRequestDto dto) {
		if (!puedeEditarOrden(idOT)) {
			return "redirect:/ordentrabajo/detalle/" + idOT + "?bloqueada=true";
		}
		dto.setFkOrdenTrabajo(idOT);
		monitoreoService.guardar(dto);
		return "redirect:/ordentrabajo/detalle/" + idOT + "?success=true";
	}

	@GetMapping("/monitoreo/eliminar/{id}/{idOT}")
	public String eliminarMonitoreo(@PathVariable int id, @PathVariable int idOT) {
		if (!puedeEditarOrden(idOT)) {
			return "redirect:/ordentrabajo/detalle/" + idOT + "?bloqueada=true";
		}
		monitoreoService.eliminar(id);
		return "redirect:/ordentrabajo/detalle/" + idOT + "?deleted=true";
	}

	@PostMapping("/desvio/guardar/{idOT}")
	public String guardarDesvio(@PathVariable int idOT, @ModelAttribute DesviosOrdenOTRequestDto dto) {
		if (!puedeEditarOrden(idOT)) {
			return "redirect:/ordentrabajo/detalle/" + idOT + "?bloqueada=true";
		}
		dto.setFkOrdenTrabajo(idOT);
		desvioService.guardar(dto);
		return "redirect:/ordentrabajo/detalle/" + idOT + "?success=true";
	}

	@GetMapping("/desvio/eliminar/{id}/{idOT}")
	public String eliminarDesvio(@PathVariable int id, @PathVariable int idOT) {
		if (!puedeEditarOrden(idOT)) {
			return "redirect:/ordentrabajo/detalle/" + idOT + "?bloqueada=true";
		}
		desvioService.eliminar(id);
		return "redirect:/ordentrabajo/detalle/" + idOT + "?deleted=true";
	}

	// ================= DESCARGA EN PDF =================

	/**
	 * Genera el PDF de la Orden de Ejecucion del Trabajo. Renderiza la plantilla
	 * ordentrabajo/ordenpdf con Thymeleaf y convierte ese HTML a PDF con openhtmltopdf.
	 */
	@GetMapping("/pdf/{idOT}")
	public ResponseEntity<byte[]> descargarPdf(@PathVariable int idOT) {
		try {
			OrdenTrabajoOTResponseDto orden = ordenService.buscarPorId(idOT);

			Context contexto = new Context(new Locale("es", "EC"));
			contexto.setVariable("orden", orden);
			contexto.setVariable("monitoreos", monitoreoService.listarPorOrden(idOT));
			contexto.setVariable("desvios", desvioService.listarPorOrden(idOT));

			String html = templateEngine.process("ordentrabajo/ordenpdf", contexto);

			ByteArrayOutputStream salida = new ByteArrayOutputStream();
			PdfRendererBuilder constructor = new PdfRendererBuilder();
			constructor.useFastMode();
			constructor.withHtmlContent(html, null);
			constructor.toStream(salida);
			constructor.run();

			HttpHeaders cabeceras = new HttpHeaders();
			cabeceras.setContentType(MediaType.APPLICATION_PDF);
			cabeceras.set(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=\"Orden-Trabajo-OT-" + idOT + ".pdf\"");

			return new ResponseEntity<>(salida.toByteArray(), cabeceras, HttpStatus.OK);

		} catch (Exception e) {
			byte[] mensaje = ("No se pudo generar el PDF: " + e.getMessage())
					.getBytes(StandardCharsets.UTF_8);
			return ResponseEntity.internalServerError()
					.contentType(MediaType.TEXT_PLAIN).body(mensaje);
		}
	}

}
