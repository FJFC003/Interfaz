package com.uisrael.prototipogestalabweb.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.AprobacionCotizacionRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CotizacionCResponseDto;
import com.uisrael.prototipogestalabweb.services.ICotizacionCService;
import com.uisrael.prototipogestalabweb.services.IEmpleadoService;

/**
 * Flujo de aprobacion de la cotizacion (Coordinadora Comercial).
 * Sin este paso la cotizacion nunca llega al Plan de Muestreo.
 */
@Controller
@RequestMapping("/cotizacion")
public class CotizacionAprobacionController {

	private final ICotizacionCService cotizacionService;
	private final IEmpleadoService empleadoService;

	public CotizacionAprobacionController(ICotizacionCService cotizacionService, IEmpleadoService empleadoService) {
		this.cotizacionService = cotizacionService;
		this.empleadoService = empleadoService;
	}

	@GetMapping("/aprobar/{id}")
	public String mostrarFormularioAprobar(@PathVariable int id, Model model) {
		try {
			CotizacionCResponseDto cotizacion = cotizacionService.buscarPorId(id);

			AprobacionCotizacionRequestDto form = new AprobacionCotizacionRequestDto();
			form.setFechaPago(new Date());

			model.addAttribute("cotizacion", cotizacion);
			model.addAttribute("aprobacion", form);
			model.addAttribute("empleados", empleadoService.listarEmpleados());
			return "cotizacion/aprobarcotizacion";
		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	@PostMapping("/aprobar/{id}")
	public String aprobar(@PathVariable int id, @ModelAttribute AprobacionCotizacionRequestDto aprobacion) {
		try {
			cotizacionService.aprobar(id, aprobacion);
			return "redirect:/cotizacion/listar?aprobada=true";
		} catch (Exception e) {
			return "redirect:/cotizacion/listar?error=true";
		}
	}

	@GetMapping("/rechazar/{id}")
	public String rechazar(@PathVariable int id) {
		try {
			cotizacionService.rechazar(id);
			return "redirect:/cotizacion/listar?rechazada=true";
		} catch (Exception e) {
			return "redirect:/cotizacion/listar?error=true";
		}
	}

}