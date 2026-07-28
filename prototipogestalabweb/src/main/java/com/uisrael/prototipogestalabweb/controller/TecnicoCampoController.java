package com.uisrael.prototipogestalabweb.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.response.EmpleadoResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.LoginResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.RecursosCronoPLResponseDto;
import com.uisrael.prototipogestalabweb.services.IEmpleadoService;
import com.uisrael.prototipogestalabweb.services.IRecursosCronoPLService;

import jakarta.servlet.http.HttpSession;

/**
 * Bandeja del Tecnico de Campo: los muestreos que le asigno la Coordinacion
 * Tecnica en la seccion "Recursos y cronograma" del plan.
 */
@Controller
@RequestMapping("/campo")
public class TecnicoCampoController {

	private final IRecursosCronoPLService recursosService;
	private final IEmpleadoService empleadoService;

	public TecnicoCampoController(IRecursosCronoPLService recursosService, IEmpleadoService empleadoService) {
		this.recursosService = recursosService;
		this.empleadoService = empleadoService;
	}

	@GetMapping("/mis-trabajos")
	public String misTrabajos(HttpSession session, Model model) {
		try {
			EmpleadoResponseDto empleado = empleadoDeLaSesion(session);

			if (empleado == null) {
				model.addAttribute("sinEmpleado", true);
				model.addAttribute("trabajos", new ArrayList<>());
				return "plan/mistrabajos";
			}

			List<RecursosCronoPLResponseDto> asignaciones =
					recursosService.listarPorTecnico(empleado.getIdEmpleado());

			// Un plan puede tener varias fechas de muestreo: se agrupa por plan
			// para que el tecnico vea una tarjeta por trabajo, no una por fecha.
			Map<Integer, List<RecursosCronoPLResponseDto>> porPlan = new LinkedHashMap<>();
			for (RecursosCronoPLResponseDto r : asignaciones) {
				if (r.getFkPlanMuestreo() != null) {
					porPlan.computeIfAbsent(r.getFkPlanMuestreo().getIdPlan(), k -> new ArrayList<>()).add(r);
				}
			}

			model.addAttribute("empleado", empleado);
			model.addAttribute("trabajosPorPlan", porPlan);
			model.addAttribute("totalAsignaciones", asignaciones.size());
			return "plan/mistrabajos";

		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	/**
	 * El login solo devuelve el idUsuario. Aqui se busca el Empleado que
	 * corresponde a ese usuario, que es lo que necesitan las consultas del plan.
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