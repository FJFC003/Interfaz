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
		try {
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

			for (RecursosCronoPLResponseDto r : asignaciones) {
				if (r.getFkPlanMuestreo() == null) {
					continue;
				}
				String estado = r.getFkPlanMuestreo().getEstadoPlan();

				// Solo entra a la bandeja lo que la Coordinacion Tecnica ya envio
				if ("ENVIADO".equalsIgnoreCase(estado) || "COMPLETADO".equalsIgnoreCase(estado)) {
					porPlan.computeIfAbsent(r.getFkPlanMuestreo().getIdPlan(), k -> new ArrayList<>()).add(r);
				} else {
					pendientesDeEnvio++;
				}
			}

			model.addAttribute("empleado", empleado);
			model.addAttribute("trabajosPorPlan", porPlan);
			model.addAttribute("totalAsignaciones", porPlan.size());
			model.addAttribute("pendientesDeEnvio", pendientesDeEnvio);
			return "plan/mistrabajos";

		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	/**
	 * El Tecnico termina el trabajo de campo y lo devuelve a Coordinacion
	 * Tecnica. El plan pasa a COMPLETADO y desde ahi se genera la OT.
	 */
	@GetMapping("/completar/{idPlan}")
	public String completar(@PathVariable int idPlan, Model model) {
		try {
			planService.marcarCompletado(idPlan);
			return "redirect:/campo/mis-trabajos?completado=true";
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

