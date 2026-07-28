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

import com.uisrael.prototipogestalabweb.model.dto.request.EEPPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.InformacionAdicionalPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.InformacionMatrizPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.ParametroAnalizarPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.PlanMuestreoPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.ProcedimientoMuePLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.RecursosCronoPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.TipoTomaFreHoraPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.VerificacionPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CotizacionCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DetalleCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EEPPLResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.PlanMuestreoPLResponseDto;
import com.uisrael.prototipogestalabweb.services.ICotizacionCService;
import com.uisrael.prototipogestalabweb.services.IDetalleCService;
import com.uisrael.prototipogestalabweb.services.IEEPPLService;
import com.uisrael.prototipogestalabweb.services.IEmpleadoService;
import com.uisrael.prototipogestalabweb.services.IInformacionAdicionalPLService;
import com.uisrael.prototipogestalabweb.services.IInformacionMatrizPLService;
import com.uisrael.prototipogestalabweb.services.IParametroAnalizarPLService;
import com.uisrael.prototipogestalabweb.services.IPlanMuestreoPLService;
import com.uisrael.prototipogestalabweb.services.IProcedimientoMuePLService;
import com.uisrael.prototipogestalabweb.services.IRecursosCronoPLService;
import com.uisrael.prototipogestalabweb.services.ITipoTomaFreHoraPLService;
import com.uisrael.prototipogestalabweb.services.IVerificacionPLService;

@Controller
@RequestMapping("/plan")
public class PlanMuestreoPLController {

	private final IPlanMuestreoPLService planService;
	private final IInformacionMatrizPLService matrizService;
	private final IParametroAnalizarPLService parametroService;
	private final ITipoTomaFreHoraPLService tipoTomaService;
	private final IProcedimientoMuePLService procedimientoService;
	private final IRecursosCronoPLService recursosService;
	private final IInformacionAdicionalPLService infoAdicionalService;
	private final IVerificacionPLService verificacionService;
	private final IEEPPLService eppService;
	private final ICotizacionCService cotizacionService;
	private final IDetalleCService detalleService;
	private final IEmpleadoService empleadoService;

	public PlanMuestreoPLController(IPlanMuestreoPLService planService, IInformacionMatrizPLService matrizService,
			IParametroAnalizarPLService parametroService, ITipoTomaFreHoraPLService tipoTomaService,
			IProcedimientoMuePLService procedimientoService, IRecursosCronoPLService recursosService,
			IInformacionAdicionalPLService infoAdicionalService, IVerificacionPLService verificacionService,
			IEEPPLService eppService, ICotizacionCService cotizacionService, IDetalleCService detalleService,
			IEmpleadoService empleadoService) {
		super();
		this.planService = planService;
		this.matrizService = matrizService;
		this.parametroService = parametroService;
		this.tipoTomaService = tipoTomaService;
		this.procedimientoService = procedimientoService;
		this.recursosService = recursosService;
		this.infoAdicionalService = infoAdicionalService;
		this.verificacionService = verificacionService;
		this.eppService = eppService;
		this.cotizacionService = cotizacionService;
		this.detalleService = detalleService;
		this.empleadoService = empleadoService;
	}

	// ================= LISTADO =================

	@GetMapping("/listar")
	public String listarPlanes(Model model) {
		model.addAttribute("planes", planService.listarPlanes());
		return "plan/listarplan";
	}

	@GetMapping("/pendientes")
	public String cotizacionesAprobadas(Model model) {
		List<CotizacionCResponseDto> aprobadas = new java.util.ArrayList<>();
		for (CotizacionCResponseDto c : cotizacionService.listarCotizaciones()) {
			if ("APROBADA".equalsIgnoreCase(c.getEstadoAprobacion())) {
				aprobadas.add(c);
			}
		}
		model.addAttribute("cotizaciones", aprobadas);
		return "plan/pendientesplan";
	}

	// ================= CREAR PLAN (con prellenado) =================

	@GetMapping("/nuevo/{idCotizacion}")
	public String mostrarFormularioNuevo(@PathVariable int idCotizacion, Model model) {
		try {
			CotizacionCResponseDto cotizacion = cotizacionService.buscarPorId(idCotizacion);
			List<DetalleCResponseDto> detalles = detalleService.listarPorCotizacion(idCotizacion);

			PlanMuestreoPLRequestDto form = new PlanMuestreoPLRequestDto();
			form.setFechaElaboracion(new Date());

			model.addAttribute("plan", form);
			model.addAttribute("cotizacion", cotizacion);
			model.addAttribute("detalles", detalles);
			model.addAttribute("empleados", empleadoService.listarEmpleados());
			return "plan/nuevoplan";
		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	@PostMapping("/guardar")
	public String guardarPlan(@ModelAttribute PlanMuestreoPLRequestDto plan) {
		PlanMuestreoPLResponseDto creado = planService.guardar(plan);
		prellenarDesdeCotizacion(creado.getIdPlan(), plan.getFkDetalleCotizacion());
		return "redirect:/plan/detalle/" + creado.getIdPlan() + "?success=true";
	}

	/**
	 * Copia al plan lo que la cotizacion ya sabe, para que la Coordinadora
	 * Tecnica no lo teclee de nuevo:
	 *  - un Parametro a Analizar por la linea de cotizacion (ensayo + unidad)
	 *  - una fila de Matriz por cada punto contratado
	 *  - una fila de Verificacion por cada punto contratado
	 * Los campos que llena el Tecnico de Campo se dejan vacios a proposito.
	 */
	private void prellenarDesdeCotizacion(int idPlan, int idDetalleC) {
		try {
			DetalleCResponseDto detalle = detalleService.buscarPorId(idDetalleC);
			if (detalle == null) {
				return;
			}

			int puntos = detalle.getCantidadPuntosDetalleC() > 0 ? detalle.getCantidadPuntosDetalleC() : 1;

			// 1. Parametro a analizar (viene del catalogo de parametros de la cotizacion)
			if (detalle.getFkParametro() != null) {
				ParametroAnalizarPLRequestDto par = new ParametroAnalizarPLRequestDto();
				par.setNoParametroPL(1);
				par.setParametros(detalle.getFkParametro().getEnsayoParametroC());
				par.setUnidadMedida(detalle.getFkParametro().getUnidadParametroC());
				par.setSitioMedicion("");   // lo completa el Tecnico de Campo
				par.setPreservacion("");    // lo completa el Tecnico de Campo
				par.setFkPlanMuestreo(idPlan);
				parametroService.guardar(par);
			}

			// 2. Una fila de matriz y una de verificacion por cada punto contratado
			for (int i = 1; i <= puntos; i++) {
				InformacionMatrizPLRequestDto mat = new InformacionMatrizPLRequestDto();
				mat.setNoItem(i);
				mat.setTipoMatriz("");
				mat.setUbicacion("");
				mat.setDescripcionDelPunto("");   // Tecnico de Campo
				mat.setAccesibilidad("");         // Tecnico de Campo
				mat.setFkPlanMuestreo(idPlan);
				matrizService.guardar(mat);

				VerificacionPLRequestDto ver = new VerificacionPLRequestDto();
				ver.setNoItem(i);
				ver.setFkPlanMuestreo(idPlan);
				verificacionService.guardar(ver);
			}
		} catch (Exception e) {
			// El prellenado es una ayuda, no debe impedir la creacion del plan.
			e.printStackTrace();
		}
	}

	// ================= DETALLE (Coordinadora Tecnica) =================

	@GetMapping("/detalle/{idPlan}")
	public String verDetalle(@PathVariable int idPlan, Model model) {
		try {
			cargarPlanCompleto(idPlan, model);
			model.addAttribute("nuevaMatriz", new InformacionMatrizPLRequestDto());
			model.addAttribute("nuevoParametro", new ParametroAnalizarPLRequestDto());
			model.addAttribute("nuevoTipoToma", new TipoTomaFreHoraPLRequestDto());
			model.addAttribute("nuevoProcedimiento", new ProcedimientoMuePLRequestDto());
			model.addAttribute("nuevoRecurso", new RecursosCronoPLRequestDto());
			model.addAttribute("nuevaInfoAdicional", new InformacionAdicionalPLRequestDto());
			model.addAttribute("nuevaVerificacion", new VerificacionPLRequestDto());
			return "plan/detalleplan";
		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	// ================= TRABAJO DE CAMPO (Tecnico) =================

	@GetMapping("/campo/{idPlan}")
	public String trabajoDeCampo(@PathVariable int idPlan, Model model) {
		try {
			cargarPlanCompleto(idPlan, model);
			return "plan/campoplan";
		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	private void cargarPlanCompleto(int idPlan, Model model) {
		PlanMuestreoPLResponseDto plan = planService.buscarPorId(idPlan);
		model.addAttribute("plan", plan);
		model.addAttribute("matrices", matrizService.listarPorPlan(idPlan));
		model.addAttribute("parametros", parametroService.listarPorPlan(idPlan));
		model.addAttribute("tiposToma", tipoTomaService.listarPorPlan(idPlan));
		model.addAttribute("procedimientos", procedimientoService.listarPorPlan(idPlan));
		model.addAttribute("recursos", recursosService.listarPorPlan(idPlan));
		model.addAttribute("infoAdicional", infoAdicionalService.listarPorPlan(idPlan));
		model.addAttribute("verificaciones", verificacionService.listarPorPlan(idPlan));
		model.addAttribute("tecnicos", empleadoService.listarEmpleados());

		EEPPLRequestDto epp = new EEPPLRequestDto();
		EEPPLResponseDto eppActual = plan.getFkeep();
		if (eppActual != null) {
			epp.setIdEEP(eppActual.getIdEEP());
			epp.setChaleco(eppActual.isChaleco());
			epp.setGafas(eppActual.isGafas());
			epp.setCasco(eppActual.isCasco());
			epp.setMandil(eppActual.isMandil());
			epp.setMascarilla(eppActual.isMascarilla());
			epp.setBotas(eppActual.isBotas());
			epp.setZapatos(eppActual.isZapatos());
			epp.setAccesoProPrivada(eppActual.isAccesoProPrivada());
		}
		model.addAttribute("epp", epp);
	}

	@GetMapping("/eliminar/{idPlan}")
	public String eliminarPlan(@PathVariable int idPlan) {
		try {
			planService.eliminar(idPlan);
			return "redirect:/plan/listar?deleted=true";
		} catch (Exception e) {
			return "redirect:/plan/listar?error=true";
		}
	}

	// ================= EPP =================

	@PostMapping("/epp/guardar/{idPlan}")
	public String guardarEpp(@PathVariable int idPlan, @ModelAttribute EEPPLRequestDto epp) {
		try {
			EEPPLResponseDto guardado = eppService.guardar(epp);

			// Enlaza el EPP al plan (el plan apunta al EPP, uno por plan)
			PlanMuestreoPLResponseDto actual = planService.buscarPorId(idPlan);
			PlanMuestreoPLRequestDto form = new PlanMuestreoPLRequestDto();
			form.setIdPlan(actual.getIdPlan());
			form.setCodigoPlan(actual.getCodigoPlan());
			form.setObjetivoPlan(actual.getObjetivoPlan());
			form.setFechaElaboracion(actual.getFechaElaboracion());
			form.setFkResponsable(actual.getFkResponsable() != null ? actual.getFkResponsable().getIdEmpleado() : 0);
			form.setFkDetalleCotizacion(
					actual.getFkDetalleCotizacion() != null ? actual.getFkDetalleCotizacion().getIdDetalleC() : 0);
			form.setFkeep(guardado.getIdEEP());
			planService.guardar(form);

			return "redirect:/plan/detalle/" + idPlan + "?success=true";
		} catch (Exception e) {
			return "redirect:/plan/detalle/" + idPlan + "?error=true";
		}
	}

	// ================= SECCIONES: alta y edicion =================
	// El backend hace upsert: si el DTO trae id, actualiza; si no, inserta.

	@PostMapping("/matriz/guardar/{idPlan}")
	public String guardarMatriz(@PathVariable int idPlan, @ModelAttribute InformacionMatrizPLRequestDto dto,
			@ModelAttribute("volverA") String volverA) {
		dto.setFkPlanMuestreo(idPlan);
		matrizService.guardar(dto);
		return redirigir(idPlan, volverA);
	}

	@GetMapping("/matriz/eliminar/{id}/{idPlan}")
	public String eliminarMatriz(@PathVariable int id, @PathVariable int idPlan) {
		matrizService.eliminar(id);
		return "redirect:/plan/detalle/" + idPlan + "?deleted=true";
	}

	@PostMapping("/parametro/guardar/{idPlan}")
	public String guardarParametro(@PathVariable int idPlan, @ModelAttribute ParametroAnalizarPLRequestDto dto,
			@ModelAttribute("volverA") String volverA) {
		dto.setFkPlanMuestreo(idPlan);
		parametroService.guardar(dto);
		return redirigir(idPlan, volverA);
	}

	@GetMapping("/parametro/eliminar/{id}/{idPlan}")
	public String eliminarParametro(@PathVariable int id, @PathVariable int idPlan) {
		parametroService.eliminar(id);
		return "redirect:/plan/detalle/" + idPlan + "?deleted=true";
	}

	@PostMapping("/tipotoma/guardar/{idPlan}")
	public String guardarTipoToma(@PathVariable int idPlan, @ModelAttribute TipoTomaFreHoraPLRequestDto dto,
			@ModelAttribute("volverA") String volverA) {
		dto.setFkPlanMuestreo(idPlan);
		tipoTomaService.guardar(dto);
		return redirigir(idPlan, volverA);
	}

	@GetMapping("/tipotoma/eliminar/{id}/{idPlan}")
	public String eliminarTipoToma(@PathVariable int id, @PathVariable int idPlan) {
		tipoTomaService.eliminar(id);
		return "redirect:/plan/detalle/" + idPlan + "?deleted=true";
	}

	@PostMapping("/procedimiento/guardar/{idPlan}")
	public String guardarProcedimiento(@PathVariable int idPlan, @ModelAttribute ProcedimientoMuePLRequestDto dto,
			@ModelAttribute("volverA") String volverA) {
		dto.setFkPlanMuestreo(idPlan);
		procedimientoService.guardar(dto);
		return redirigir(idPlan, volverA);
	}

	@GetMapping("/procedimiento/eliminar/{id}/{idPlan}")
	public String eliminarProcedimiento(@PathVariable int id, @PathVariable int idPlan) {
		procedimientoService.eliminar(id);
		return "redirect:/plan/detalle/" + idPlan + "?deleted=true";
	}

	@PostMapping("/recurso/guardar/{idPlan}")
	public String guardarRecurso(@PathVariable int idPlan, @ModelAttribute RecursosCronoPLRequestDto dto) {
		dto.setFkPlanMuestreo(idPlan);
		recursosService.guardar(dto);
		return "redirect:/plan/detalle/" + idPlan + "?success=true";
	}

	@GetMapping("/recurso/eliminar/{id}/{idPlan}")
	public String eliminarRecurso(@PathVariable int id, @PathVariable int idPlan) {
		recursosService.eliminar(id);
		return "redirect:/plan/detalle/" + idPlan + "?deleted=true";
	}

	@PostMapping("/infoadicional/guardar/{idPlan}")
	public String guardarInfoAdicional(@PathVariable int idPlan, @ModelAttribute InformacionAdicionalPLRequestDto dto) {
		dto.setFkPlanMuestreo(idPlan);
		infoAdicionalService.guardar(dto);
		return "redirect:/plan/detalle/" + idPlan + "?success=true";
	}

	@GetMapping("/infoadicional/eliminar/{id}/{idPlan}")
	public String eliminarInfoAdicional(@PathVariable int id, @PathVariable int idPlan) {
		infoAdicionalService.eliminar(id);
		return "redirect:/plan/detalle/" + idPlan + "?deleted=true";
	}

	@PostMapping("/verificacion/guardar/{idPlan}")
	public String guardarVerificacion(@PathVariable int idPlan, @ModelAttribute VerificacionPLRequestDto dto,
			@ModelAttribute("volverA") String volverA) {
		dto.setFkPlanMuestreo(idPlan);
		verificacionService.guardar(dto);
		return redirigir(idPlan, volverA);
	}

	@GetMapping("/verificacion/eliminar/{id}/{idPlan}")
	public String eliminarVerificacion(@PathVariable int id, @PathVariable int idPlan) {
		verificacionService.eliminar(id);
		return "redirect:/plan/detalle/" + idPlan + "?deleted=true";
	}

	// Devuelve al usuario a la pantalla desde la que guardo:
	// "campo" para el Tecnico de Campo, detalle para la Coordinadora Tecnica.
	private String redirigir(int idPlan, String volverA) {
		if ("campo".equalsIgnoreCase(volverA)) {
			return "redirect:/plan/campo/" + idPlan + "?success=true";
		}
		return "redirect:/plan/detalle/" + idPlan + "?success=true";
	}

}
