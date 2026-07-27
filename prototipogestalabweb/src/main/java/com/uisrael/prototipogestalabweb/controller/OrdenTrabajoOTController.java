package com.uisrael.prototipogestalabweb.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.OrdenTrabajoOTRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.OrdenTrabajoOTResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.PlanMuestreoPLResponseDto;
import com.uisrael.prototipogestalabweb.services.IEmpleadoService;
import com.uisrael.prototipogestalabweb.services.IOrdenTrabajoOTService;
import com.uisrael.prototipogestalabweb.services.IPlanMuestreoPLService;

@Controller
@RequestMapping("/ordentrabajo")
public class OrdenTrabajoOTController {

	private final IOrdenTrabajoOTService ordenService;
	private final IPlanMuestreoPLService planService;
	private final IEmpleadoService empleadoService;

	public OrdenTrabajoOTController(IOrdenTrabajoOTService ordenService, IPlanMuestreoPLService planService,
			IEmpleadoService empleadoService) {
		this.ordenService = ordenService;
		this.planService = planService;
		this.empleadoService = empleadoService;
	}

	@GetMapping("/listar")
	public String listarOrdenes(Model model) {
		model.addAttribute("ordenes", ordenService.listarOrdenes());
		return "ordentrabajo/listarorden";
	}

	// Planes de muestreo que todavia pueden generar una orden de trabajo.
	@GetMapping("/pendientes")
	public String planesDisponibles(Model model) {
		model.addAttribute("planes", planService.listarPlanes());
		return "ordentrabajo/pendientesorden";
	}

	@GetMapping("/nuevo/{idPlan}")
	public String mostrarFormularioNuevo(@PathVariable int idPlan, Model model) {
		try {
			PlanMuestreoPLResponseDto plan = planService.buscarPorId(idPlan);

			OrdenTrabajoOTRequestDto form = new OrdenTrabajoOTRequestDto();
			form.setFkPlanMuestreo(idPlan);
			form.setFechaEmisionOT(new Date());
			form.setNoItemOT(1);

			model.addAttribute("orden", form);
			model.addAttribute("plan", plan);
			model.addAttribute("empleados", empleadoService.listarEmpleados());
			return "ordentrabajo/nuevaorden";
		} catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/guardar")
	public String guardarOrden(@ModelAttribute OrdenTrabajoOTRequestDto orden) {
		OrdenTrabajoOTResponseDto creada = ordenService.guardar(orden);
		return "redirect:/ordentrabajo/detalle/" + creada.getIdOT() + "?success=true";
	}

	@GetMapping("/detalle/{idOT}")
	public String verDetalle(@PathVariable int idOT, Model model) {
		try {
			model.addAttribute("orden", ordenService.buscarPorId(idOT));
			return "ordentrabajo/detalleorden";
		} catch (Exception e) {
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

}
