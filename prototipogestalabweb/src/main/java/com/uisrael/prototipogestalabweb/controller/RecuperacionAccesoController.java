package com.uisrael.prototipogestalabweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.uisrael.prototipogestalabweb.model.dto.request.PreguntaSeguridadRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.RestablecerAccesoRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.PreguntaSeguridadResponseDto;
import com.uisrael.prototipogestalabweb.services.IRecuperacionAccesoService;

import jakarta.servlet.http.HttpSession;

/**
 * Flujo publico de recuperacion de acceso. No requiere sesion iniciada, por eso
 * la ruta /recuperar esta excluida en WebMvcConfig.
 *
 * En ningun paso se muestra la contrasena anterior: no existe en la base de
 * datos, solo su hash BCrypt. Lo que se hace es fijar una nueva.
 */
@Controller
@RequestMapping("/recuperar")
public class RecuperacionAccesoController {

	private static final String SESION_CORREO = "recuperacionCorreo";
	private static final String SESION_PREGUNTA = "recuperacionPregunta";

	private final IRecuperacionAccesoService recuperacionService;

	public RecuperacionAccesoController(IRecuperacionAccesoService recuperacionService) {
		super();
		this.recuperacionService = recuperacionService;
	}

	/** Paso 1: pedir el correo. */
	@GetMapping
	public String mostrarSolicitud(Model model) {
		model.addAttribute("solicitud", new PreguntaSeguridadRequestDto());
		return "seguridad/recuperar";
	}

	@PostMapping
	public String buscarPregunta(@ModelAttribute PreguntaSeguridadRequestDto solicitud,
			HttpSession session, Model model) {
		try {
			PreguntaSeguridadResponseDto respuesta = recuperacionService.obtenerPregunta(solicitud);

			session.setAttribute(SESION_CORREO, respuesta.getCorreo());
			session.setAttribute(SESION_PREGUNTA, respuesta.getPregunta());
			return "redirect:/recuperar/responder";

		} catch (WebClientResponseException.Unauthorized ex) {
			model.addAttribute("error",
					"No existe una cuenta de Gerente General activa con ese correo.");
		} catch (WebClientResponseException.Conflict ex) {
			model.addAttribute("error",
					"Esa cuenta todavia no tiene configurada una pregunta de seguridad. "
					+ "Contacte al administrador del sistema.");
		} catch (WebClientResponseException.BadRequest ex) {
			model.addAttribute("error", "Debe escribir un correo.");
		} catch (Exception ex) {
			model.addAttribute("error", "No se pudo conectar con el servidor. Intente nuevamente.");
		}

		model.addAttribute("solicitud", solicitud);
		return "seguridad/recuperar";
	}

	/** Paso 2: mostrar la pregunta y pedir respuesta + contrasena nueva. */
	@GetMapping("/responder")
	public String mostrarRespuesta(HttpSession session, Model model) {
		String correo = (String) session.getAttribute(SESION_CORREO);
		String pregunta = (String) session.getAttribute(SESION_PREGUNTA);

		if (correo == null || pregunta == null) {
			return "redirect:/recuperar";
		}

		RestablecerAccesoRequestDto datos = new RestablecerAccesoRequestDto();
		datos.setCorreo(correo);

		model.addAttribute("datos", datos);
		model.addAttribute("pregunta", pregunta);
		return "seguridad/responder";
	}

	@PostMapping("/responder")
	public String procesarRespuesta(@ModelAttribute RestablecerAccesoRequestDto datos,
			HttpSession session, Model model) {

		String correo = (String) session.getAttribute(SESION_CORREO);
		String pregunta = (String) session.getAttribute(SESION_PREGUNTA);

		if (correo == null || pregunta == null) {
			return "redirect:/recuperar";
		}

		// El correo siempre sale de la sesion, nunca del formulario.
		datos.setCorreo(correo);
		model.addAttribute("pregunta", pregunta);

		String nueva = datos.getNuevaContrasenia() == null ? "" : datos.getNuevaContrasenia();
		String confirmacion = datos.getConfirmacionContrasenia() == null
				? "" : datos.getConfirmacionContrasenia();

		if (!nueva.equals(confirmacion)) {
			model.addAttribute("error", "Las dos contrasenas no coinciden.");
			model.addAttribute("datos", datos);
			return "seguridad/responder";
		}

		if (nueva.trim().length() < 8) {
			model.addAttribute("error", "La nueva contrasena debe tener al menos 8 caracteres.");
			model.addAttribute("datos", datos);
			return "seguridad/responder";
		}

		try {
			recuperacionService.restablecer(datos);

			session.removeAttribute(SESION_CORREO);
			session.removeAttribute(SESION_PREGUNTA);
			return "redirect:/login?restablecida=true";

		} catch (WebClientResponseException.Unauthorized ex) {
			model.addAttribute("error", "La respuesta no coincide con la registrada.");
		} catch (WebClientResponseException.Conflict ex) {
			model.addAttribute("error", ex.getResponseBodyAsString());
		} catch (Exception ex) {
			model.addAttribute("error", "No se pudo conectar con el servidor. Intente nuevamente.");
		}

		model.addAttribute("datos", datos);
		return "seguridad/responder";
	}

}
