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
import com.uisrael.prototipogestalabweb.model.dto.response.EmpleadoResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.LoginResponseDto;
import com.uisrael.prototipogestalabweb.services.ICotizacionCService;
import com.uisrael.prototipogestalabweb.services.IEmpleadoService;

import jakarta.servlet.http.HttpSession;

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
	public String mostrarFormularioAprobar(@PathVariable int id, Model model, HttpSession session) {
		try {
			CotizacionCResponseDto cotizacion = cotizacionService.buscarPorId(id);

			AprobacionCotizacionRequestDto form = new AprobacionCotizacionRequestDto();
			form.setFechaPago(new Date());

			// Quien aprueba es siempre la persona con la sesion abierta: no se
			// elige de una lista, igual que "Elaborado Por" en Nueva Cotizacion.
			EmpleadoResponseDto empleadoActual = empleadoDeLaSesion(session);
			if (empleadoActual != null) {
				form.setFkEmpleadoAprueba(empleadoActual.getIdEmpleado());
				model.addAttribute("empleadoActual", empleadoActual);
			}

			model.addAttribute("cotizacion", cotizacion);
			model.addAttribute("aprobacion", form);
			return "cotizacion/aprobarcotizacion";
		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	@PostMapping("/aprobar/{id}")
	public String aprobar(@PathVariable int id, @ModelAttribute AprobacionCotizacionRequestDto aprobacion,
			HttpSession session) {
		try {
			// El aprobador se vuelve a tomar de la sesion y no del formulario:
			// asi nadie puede aprobar a nombre de otro editando el HTML.
			EmpleadoResponseDto empleadoActual = empleadoDeLaSesion(session);
			if (empleadoActual == null) {
				return "redirect:/cotizacion/listar?sinempleado=true";
			}
			aprobacion.setFkEmpleadoAprueba(empleadoActual.getIdEmpleado());

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

	/**
	 * Devuelve la ficha de empleado del usuario que tiene la sesion abierta, o
	 * null si esa cuenta no tiene ficha (por ejemplo un usuario creado a mano en
	 * la base sin su fila en la tabla empleado).
	 */
	private EmpleadoResponseDto empleadoDeLaSesion(HttpSession session) {
		Object usuarioObj = session.getAttribute("usuarioActual");
		if (usuarioObj instanceof LoginResponseDto usuarioActual) {
			return empleadoService.listarEmpleados().stream()
					.filter(e -> e.getFkUsuario() != null
							&& e.getFkUsuario().getIdUsuario() == usuarioActual.getIdUsuario())
					.findFirst()
					.orElse(null);
		}
		return null;
	}

}