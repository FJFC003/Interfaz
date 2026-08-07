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
import org.springframework.web.reactive.function.client.WebClientResponseException;

import jakarta.servlet.http.HttpSession;

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
import com.uisrael.prototipogestalabweb.model.dto.response.EmpleadoResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.LoginResponseDto;
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

	private static final java.util.List<String> PREGUNTAS_INFORMACION_ADICIONAL = java.util.List.of(
			"Se verificó que no existen más descargas de las declaradas / detallar",
			"Las descargas se realizan en: alcantarillado, efluente, detallar",
			"Describir los procesos de tratamientos de las muestras colectadas",
			"Descripción tipo de transporte de muestras colectadas");


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


	@GetMapping("/pendientes")
	public String cotizacionesAprobadas(Model model) {
		List<CotizacionCResponseDto> aprobadas = new java.util.ArrayList<>();
		for (CotizacionCResponseDto c : cotizacionService.listarCotizaciones()) {
			if ("APROBADA".equalsIgnoreCase(c.getEstadoAprobacion())) {
				aprobadas.add(c);
			}
		}
		model.addAttribute("cotizaciones", aprobadas);
		// Para cada cotizacion que ya tiene plan se guarda el codigo del plan.
		// La pantalla usa este mapa para decidir si muestra el boton Crear Plan
		// o el aviso de que ya esta elaborado.
		model.addAttribute("planPorCotizacion", planesPorCotizacion());
		return "plan/pendientesplan";
	}

	/**
	 * Cotizaciones que ya tienen Plan de Muestreo, con el codigo del plan.
	 *
	 * El plan no apunta a la cotizacion sino a una de sus lineas de detalle
	 * (fkDetalleCotizacion), asi que hay que subir un nivel mas para saber a
	 * que cotizacion pertenece: plan -> detalle -> cotizacion.
	 */
	private java.util.Map<Integer, String> planesPorCotizacion() {
		java.util.Map<Integer, String> mapa = new java.util.HashMap<>();
		try {
			for (PlanMuestreoPLResponseDto plan : planService.listarPlanes()) {
				if (plan.getFkDetalleCotizacion() == null
						|| plan.getFkDetalleCotizacion().getFkCotizacion() == null) {
					continue;
				}
				int idCotizacion = plan.getFkDetalleCotizacion().getFkCotizacion().getIdCotizacionC();
				String codigo = plan.getCodigoPlan() != null && !plan.getCodigoPlan().isBlank()
						? plan.getCodigoPlan()
						: "Plan " + plan.getIdPlan();
				// Si por datos antiguos hubiera mas de un plan para la misma
				// cotizacion, se conserva el primero que aparece.
				mapa.putIfAbsent(idCotizacion, codigo);
			}
		} catch (Exception e) {
			// Si el backend de planes no responde, la pantalla sigue mostrando
			// el boton Crear Plan en vez de romperse.
			return mapa;
		}
		return mapa;
	}


	@GetMapping("/nuevo/{idCotizacion}")
	public String mostrarFormularioNuevo(@PathVariable int idCotizacion, HttpSession session, Model model) {
		try {
			// Una cotizacion tiene un solo Plan de Muestreo. Aunque la pantalla
			// ya no muestre el boton, la ruta sigue siendo accesible escribiendo
			// la direccion a mano, asi que se comprueba tambien aqui.
			if (planesPorCotizacion().containsKey(idCotizacion)) {
				return "redirect:/plan/pendientes?yatiene=true";
			}

			CotizacionCResponseDto cotizacion = cotizacionService.buscarPorId(idCotizacion);
			List<DetalleCResponseDto> detalles = detalleService.listarPorCotizacion(idCotizacion);

			PlanMuestreoPLRequestDto form = new PlanMuestreoPLRequestDto();
			// Fecha de elaboracion: siempre el dia en curso.
			form.setFechaElaboracion(new Date());

			// Responsable: el empleado que tiene la sesion abierta. No se elige.
			EmpleadoResponseDto responsable = empleadoDeLaSesion(session);
			if (responsable != null) {
				form.setFkResponsable(responsable.getIdEmpleado());
			}

			model.addAttribute("plan", form);
			model.addAttribute("cotizacion", cotizacion);
			model.addAttribute("detalles", detalles);
			model.addAttribute("responsable", responsable);
			return "plan/nuevoplan";
		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	/**
	 * Empleado ligado al usuario de la sesion. Se usa para asignar el responsable
	 * del plan sin dejar que se elija a otra persona de una lista.
	 */
	private EmpleadoResponseDto empleadoDeLaSesion(HttpSession session) {
		Object attr = session != null ? session.getAttribute("usuarioActual") : null;
		if (!(attr instanceof LoginResponseDto usuario)) {
			return null;
		}
		for (EmpleadoResponseDto emp : empleadoService.listarEmpleados()) {
			if (emp.getFkUsuario() != null && emp.getFkUsuario().getIdUsuario() == usuario.getIdUsuario()) {
				return emp;
			}
		}
		return null;
	}

	@PostMapping("/guardar")
	public String guardarPlan(@ModelAttribute PlanMuestreoPLRequestDto plan,
			HttpSession session, Model model) {
		try {
			// Misma regla que en /plan/nuevo/{id}, ahora sobre el envio: si la
			// cotizacion de esa linea de detalle ya tiene plan, no se crea otro.
			DetalleCResponseDto lineaElegida = detalleService.buscarPorId(plan.getFkDetalleCotizacion());
			if (lineaElegida != null && lineaElegida.getFkCotizacion() != null
					&& planesPorCotizacion().containsKey(lineaElegida.getFkCotizacion().getIdCotizacionC())) {
				return "redirect:/plan/pendientes?yatiene=true";
			}

			// Los tres datos que el sistema controla se fijan aqui, no se toman
			// del formulario: fecha de hoy, responsable de la sesion y la
			// identificacion, que la genera el backend cuando llega en blanco.
			plan.setFechaElaboracion(new Date());
			plan.setCodigoPlan(null);

			EmpleadoResponseDto responsable = empleadoDeLaSesion(session);
			if (responsable != null) {
				plan.setFkResponsable(responsable.getIdEmpleado());
			}

			PlanMuestreoPLResponseDto creado = planService.guardar(plan);
			prellenarDesdeCotizacion(creado.getIdPlan(), plan.getFkDetalleCotizacion());
			return "redirect:/plan/detalle/" + creado.getIdPlan() + "?success=true";

		} catch (WebClientResponseException ex) {
			// El backend devuelve el detalle del error en el cuerpo de la
			// respuesta. Se muestra en pantalla para no tener que ir a la
			// consola cada vez.
			model.addAttribute("mensajeError", ex.getResponseBodyAsString());
			return "error";

		} catch (Exception ex) {
			model.addAttribute("mensajeError",
					ex.getClass().getSimpleName() + " - " + ex.getMessage());
			return "error";
		}
	}

	
	private void prellenarDesdeCotizacion(int idPlan, int idDetalleC) {
		try {
			DetalleCResponseDto detalle = detalleService.buscarPorId(idDetalleC);
			if (detalle == null) {
				return;
			}

			int puntos = detalle.getCantidadPuntosDetalleC() > 0 ? detalle.getCantidadPuntosDetalleC() : 1;

			String tipoMatriz = (detalle.getFkDescripcionServicio() != null
					&& detalle.getFkDescripcionServicio().getTextoDescripcionServicioC() != null)
							? detalle.getFkDescripcionServicio().getTextoDescripcionServicioC()
							: "Por definir";

			String ubicacion = "Por definir";
			if (detalle.getFkCotizacion() != null && detalle.getFkCotizacion().getFkCliente() != null
					&& detalle.getFkCotizacion().getFkCliente().getDireccionClienteC() != null) {
				ubicacion = detalle.getFkCotizacion().getFkCliente().getDireccionClienteC();
			}

			// 1. PARAMETROS A ANALIZAR <- Ensayo/parametro y Unidad de la cotizacion
			if (detalle.getFkParametro() != null) {
				ParametroAnalizarPLRequestDto par = new ParametroAnalizarPLRequestDto();
				par.setNoParametroPL(1);
				par.setParametros(detalle.getFkParametro().getEnsayoParametroC());
				par.setUnidadMedida(detalle.getFkParametro().getUnidadParametroC());
				par.setSitioMedicion(null);   // lo completa el Tecnico de Campo
				par.setPreservacion(null);    // lo completa el Tecnico de Campo
				par.setFkPlanMuestreo(idPlan);
				parametroService.guardar(par);
			}

			for (int i = 1; i <= puntos; i++) {
				InformacionMatrizPLRequestDto mat = new InformacionMatrizPLRequestDto();
				mat.setNoItem(i);
				mat.setTipoMatriz(tipoMatriz);
				mat.setUbicacion(ubicacion);
				mat.setDescripcionDelPunto(null);   // Tecnico de Campo
				mat.setAccesibilidad(null);         // Tecnico de Campo
				mat.setFkPlanMuestreo(idPlan);
				matrizService.guardar(mat);

				VerificacionPLRequestDto ver = new VerificacionPLRequestDto();
				ver.setNoItem(i);
				ver.setFkPlanMuestreo(idPlan);
				verificacionService.guardar(ver);
			}

			ProcedimientoMuePLRequestDto proc = new ProcedimientoMuePLRequestDto();
			proc.setNoItem(1);
			proc.setTipo(tipoMatriz);
			proc.setDescripcion(null);            // Tecnico de Campo
			proc.setPrecausiones("Uso de EPP");
			proc.setFkPlanMuestreo(idPlan);
			procedimientoService.guardar(proc);

			for (String pregunta : PREGUNTAS_INFORMACION_ADICIONAL) {
				InformacionAdicionalPLRequestDto info = new InformacionAdicionalPLRequestDto();
				info.setPreguntas(pregunta);
				info.setRespuesta(null);
				info.setFkPlanMuestreo(idPlan);
				infoAdicionalService.guardar(info);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}



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

	// ================= ENVIO AL TECNICO DE CAMPO =================

	/**
	 * Marca el plan como ENVIADO. Recien entonces aparece en la bandeja del
	 * Técnico de Campo. Antes se valida que tenga al menos un técnico asignado
	 * en Recursos y cronograma: sin eso, nadie lo recibiría.
	 */
	@GetMapping("/enviar/{idPlan}")
	public String enviarATecnico(@PathVariable int idPlan) {
		try {
			if (recursosService.listarPorPlan(idPlan).isEmpty()) {
				return "redirect:/plan/detalle/" + idPlan + "?sinTecnico=true";
			}
			planService.enviarATecnico(idPlan);
			return "redirect:/plan/detalle/" + idPlan + "?enviado=true";
		} catch (Exception e) {
			return "redirect:/plan/detalle/" + idPlan + "?error=true";
		}
	}

	/** Devuelve el plan a elaboracion: deja de verlo el Técnico de Campo. */
	@GetMapping("/devolver/{idPlan}")
	public String devolverAElaboracion(@PathVariable int idPlan) {
		try {
			planService.devolverAElaboracion(idPlan);
			return "redirect:/plan/detalle/" + idPlan + "?devuelto=true";
		} catch (Exception e) {
			return "redirect:/plan/detalle/" + idPlan + "?error=true";
		}
	}

	@GetMapping("/eliminar/{idPlan}")
	public String eliminarPlan(@PathVariable int idPlan) {
		try {
			planService.eliminar(idPlan);
			return "redirect:/plan/listar?deleted=true";
		} catch (Exception e) {
			// La causa habitual: el plan tiene Ordenes de Trabajo emitidas,
			// que no se borran en cascada porque son documentos con valor propio.
			return "redirect:/plan/listar?errorEliminar=true";
		}
	}


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
	// "campo" para el Técnico de Campo, detalle para la Coordinadora Técnica.
	private String redirigir(int idPlan, String volverA) {
		if ("campo".equalsIgnoreCase(volverA)) {
			return "redirect:/plan/campo/" + idPlan + "?success=true";
		}
		return "redirect:/plan/detalle/" + idPlan + "?success=true";
	}

}
