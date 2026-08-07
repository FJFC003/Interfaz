package com.uisrael.prototipogestalabweb.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.response.EmpleadoResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.LoginResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.RecursosCronoPLResponseDto;
import com.uisrael.prototipogestalabweb.services.IEmpleadoService;
import com.uisrael.prototipogestalabweb.services.IPlanMuestreoPLService;
import com.uisrael.prototipogestalabweb.services.IRecursosCronoPLService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/campo")
public class TecnicoCampoController {

	private final IRecursosCronoPLService recursosService;
	private final IEmpleadoService empleadoService;
	private final IPlanMuestreoPLService planService;

	public TecnicoCampoController(IRecursosCronoPLService recursosService, IEmpleadoService empleadoService,
			IPlanMuestreoPLService planService) {
		this.recursosService = recursosService;
		this.empleadoService = empleadoService;
		this.planService = planService;
	}

	@GetMapping("/mis-trabajos")
	public String misTrabajos(HttpSession session, Model model) {
		return bandeja(session, model, "todos");
	}

	/**
	 * Trabajos que el Tecnico todavia no ha enviado a Coordinacion Tecnica
	 * (plan en estado ENVIADO). Es la unica bandeja desde la que se registra.
	 */
	@GetMapping("/pendientes")
	public String trabajosPendientes(HttpSession session, Model model) {
		return bandeja(session, model, "pendientes");
	}

	/**
	 * Trabajos ya completados y devueltos a Coordinacion Tecnica
	 * (plan en estado COMPLETADO). Solo consulta e impresion.
	 */
	@GetMapping("/completados")
	public String trabajosCompletados(HttpSession session, Model model) {
		return bandeja(session, model, "completados");
	}

	/**
	 * Las tres bandejas comparten pantalla y datos; lo unico que cambia es que
	 * planes entran y que botones se muestran. El modo viaja al HTML para que
	 * el Tecnico vea siempre la misma tarjeta y no se desoriente.
	 */
	private String bandeja(HttpSession session, Model model, String modo) {
		try {
			model.addAttribute("modo", modo);

			EmpleadoResponseDto empleado = empleadoDeLaSesion(session);

			if (empleado == null) {
				model.addAttribute("sinEmpleado", true);
				model.addAttribute("trabajosPorPlan", new LinkedHashMap<>());
				model.addAttribute("totalAsignaciones", 0);
				return "plan/mistrabajos";
			}

			List<RecursosCronoPLResponseDto> asignaciones =
					recursosService.listarPorTecnico(empleado.getIdEmpleado());

			// Un plan puede tener varias fechas de muestreo: se agrupa por plan
			// para que el tecnico vea un trabajo, no una fila por fecha.
			Map<Integer, List<RecursosCronoPLResponseDto>> porPlan = new LinkedHashMap<>();
			int pendientesDeEnvio = 0;
			int totalPendientes = 0;
			int totalCompletados = 0;

			for (RecursosCronoPLResponseDto r : asignaciones) {
				if (r.getFkPlanMuestreo() == null) {
					continue;
				}
				String estado = r.getFkPlanMuestreo().getEstadoPlan();
				boolean enviado = "ENVIADO".equalsIgnoreCase(estado);
				boolean completado = "COMPLETADO".equalsIgnoreCase(estado);

				// Lo que la Coordinacion Tecnica aun no envia no entra a ninguna
				// bandeja: el Tecnico no debe verlo todavia.
				if (!enviado && !completado) {
					pendientesDeEnvio++;
					continue;
				}

				if (enviado) {
					totalPendientes++;
				} else {
					totalCompletados++;
				}

				if (entraEnLaBandeja(modo, enviado, completado)) {
					porPlan.computeIfAbsent(r.getFkPlanMuestreo().getIdPlan(), k -> new ArrayList<>()).add(r);
				}
			}

			model.addAttribute("empleado", empleado);
			model.addAttribute("trabajosPorPlan", porPlan);
			model.addAttribute("totalAsignaciones", porPlan.size());
			model.addAttribute("pendientesDeEnvio", pendientesDeEnvio);
			// Contadores para los avisos de las pantallas vacias.
			model.addAttribute("totalPendientes", totalPendientes);
			model.addAttribute("totalCompletados", totalCompletados);
			return "plan/mistrabajos";

		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	private boolean entraEnLaBandeja(String modo, boolean enviado, boolean completado) {
		if ("pendientes".equals(modo)) {
			return enviado;
		}
		if ("completados".equals(modo)) {
			return completado;
		}
		return enviado || completado;
	}

	/**
	 * El Tecnico termina el trabajo de campo y lo devuelve a Coordinacion
	 * Tecnica. El plan pasa a COMPLETADO y desde ahi se genera la OT.
	 */
	@GetMapping("/completar/{idPlan}")
	public String completar(@PathVariable int idPlan, Model model) {
		try {
			planService.marcarCompletado(idPlan);
			// Vuelve a Pendientes: es la bandeja desde la que se envio, y el
			// trabajo ya no aparecera ahi sino en Completados.
			return "redirect:/campo/pendientes?completado=true";
		} catch (Exception e) {
			model.addAttribute("mensajeError",
					e.getClass().getSimpleName() + " - " + e.getMessage());
			return "error";
		}
	}

	/**
	 * El login solo devuelve el idUsuario, pero las asignaciones apuntan a
	 * Empleado. Aqui se busca el empleado que corresponde al usuario en sesion.
	 */
	private EmpleadoResponseDto empleadoDeLaSesion(HttpSession session) {
		Object attr = session.getAttribute("usuarioActual");
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

}

