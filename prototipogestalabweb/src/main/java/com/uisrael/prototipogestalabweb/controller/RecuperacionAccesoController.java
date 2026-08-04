package com.uisrael.prototipogestalabweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.uisrael.prototipogestalabweb.model.dto.request.RestablecerConTokenRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.SolicitarRecuperacionRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.SolicitudRecuperacionResponseDto;
import com.uisrael.prototipogestalabweb.services.IRecuperacionAccesoService;

/**
 * Flujo publico de recuperacion de acceso por correo. No requiere sesion
 * iniciada, por eso /recuperar esta excluido en WebMvcConfig.
 *
 * En ningun paso se muestra la contrasena anterior: no existe en la base de
 * datos, solo su hash BCrypt. Lo que se hace es fijar una nueva.
 */
@Controller
@RequestMapping("/recuperar")
public class RecuperacionAccesoController {

	private final IRecuperacionAccesoService recuperacionService;

	public RecuperacionAccesoController(IRecuperacionAccesoService recuperacionService) {
		super();
		this.recuperacionService = recuperacionService;
	}

	/** Paso 1: pedir el correo laboral. */
	@GetMapping
	public String mostrarSolicitud(Model model) {
		model.addAttribute("solicitud", new SolicitarRecuperacionRequestDto());
		return "seguridad/recuperar";
	}

	@PostMapping
	public String enviarEnlace(@ModelAttribute SolicitarRecuperacionRequestDto solicitud,
			Model model) {
		try {
			SolicitudRecuperacionResponseDto respuesta = recuperacionService.solicitar(solicitud);

			model.addAttribute("enviado", true);
			model.addAttribute("correoEnmascarado", respuesta.getCorreoEnmascarado());
			model.addAttribute("minutosValidez", respuesta.getMinutosValidez());

		} catch (WebClientResponseException.Unauthorized ex) {
			model.addAttribute("error",
					"No se pudo iniciar la recuperación con ese correo. "
					+ "Verifique que sea el correo laboral de la Gerente General.");
		} catch (WebClientResponseException.Conflict ex) {
			model.addAttribute("error", ex.getResponseBodyAsString());
		} catch (WebClientResponseException.BadRequest ex) {
			model.addAttribute("error", "Debe escribir su correo laboral.");
		} catch (WebClientResponseException.InternalServerError ex) {
			// Tipicamente: el servidor de correo no respondio o las credenciales
			// de envio no estan configuradas.
			model.addAttribute("error",
					"No se pudo enviar el correo. Revise la conexión a internet "
					+ "e inténtelo de nuevo.");
		} catch (Exception ex) {
			model.addAttribute("error", "No se pudo conectar con el servidor. Intente nuevamente.");
		}

		model.addAttribute("solicitud", solicitud);
		return "seguridad/recuperar";
	}

	/** Paso 2: el enlace del correo aterriza aqui. */
	@GetMapping("/token/{token}")
	public String mostrarNuevaContrasenia(@PathVariable String token, Model model) {
		try {
			recuperacionService.validarToken(token);

			RestablecerConTokenRequestDto datos = new RestablecerConTokenRequestDto();
			datos.setToken(token);
			model.addAttribute("datos", datos);
			return "seguridad/nuevacontrasenia";

		} catch (WebClientResponseException.Conflict ex) {
			model.addAttribute("error", ex.getResponseBodyAsString());
		} catch (WebClientResponseException.Unauthorized ex) {
			model.addAttribute("error", "El enlace no es válido. Solicite uno nuevo.");
		} catch (Exception ex) {
			model.addAttribute("error", "No se pudo comprobar el enlace. Intente nuevamente.");
		}

		model.addAttribute("solicitud", new SolicitarRecuperacionRequestDto());
		return "seguridad/recuperar";
	}

	@PostMapping("/token")
	public String guardarNuevaContrasenia(@ModelAttribute RestablecerConTokenRequestDto datos,
			Model model) {

		String nueva = datos.getNuevaContrasenia() == null ? "" : datos.getNuevaContrasenia();
		String confirmacion = datos.getConfirmacionContrasenia() == null
				? "" : datos.getConfirmacionContrasenia();

		if (!nueva.equals(confirmacion)) {
			model.addAttribute("error", "Las dos contraseñas no coinciden.");
			model.addAttribute("datos", datos);
			return "seguridad/nuevacontrasenia";
		}

		if (nueva.trim().length() < 8) {
			model.addAttribute("error", "La nueva contraseña debe tener al menos 8 caracteres.");
			model.addAttribute("datos", datos);
			return "seguridad/nuevacontrasenia";
		}

		try {
			recuperacionService.restablecer(datos);
			return "redirect:/login?restablecida=true";

		} catch (WebClientResponseException.Conflict ex) {
			model.addAttribute("error", ex.getResponseBodyAsString());
		} catch (WebClientResponseException.Unauthorized ex) {
			model.addAttribute("error", "El enlace no es válido. Solicite uno nuevo.");
		} catch (Exception ex) {
			model.addAttribute("error", "No se pudo guardar la contraseña. Intente nuevamente.");
		}

		model.addAttribute("datos", datos);
		return "seguridad/nuevacontrasenia";
	}

}