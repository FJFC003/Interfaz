package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.uisrael.prototipogestalabweb.model.dto.request.ConfigurarPreguntaRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.LoginResponseDto;
import com.uisrael.prototipogestalabweb.services.IRecuperacionAccesoService;

import jakarta.servlet.http.HttpSession;

/**
 * Pantalla donde la Gerente General configura o cambia su pregunta de
 * seguridad. Requiere sesion iniciada y el rol correcto (lo verifica
 * SeguridadInterceptor sobre el prefijo /seguridad).
 */
@Controller
@RequestMapping("/seguridad")
public class PreguntaSeguridadController {

	/** Lista fija: evita que se escriban preguntas imposibles de recordar. */
	private static final List<String> PREGUNTAS = List.of(
			"Cual es el nombre de su primera mascota?",
			"En que ciudad nacio?",
			"Cual es el nombre de su mejor amigo de la infancia?",
			"Cual fue su primer trabajo?",
			"Cual es el segundo nombre de su madre?",
			"Cual es el nombre de la escuela donde estudio la primaria?");

	private final IRecuperacionAccesoService recuperacionService;

	public PreguntaSeguridadController(IRecuperacionAccesoService recuperacionService) {
		super();
		this.recuperacionService = recuperacionService;
	}

	@GetMapping("/pregunta")
	public String mostrar(HttpSession session, Model model) {
		LoginResponseDto sesion = (LoginResponseDto) session.getAttribute("usuarioActual");
		if (sesion == null) {
			return "redirect:/login";
		}

		model.addAttribute("preguntas", PREGUNTAS);
		model.addAttribute("configuracion", new ConfigurarPreguntaRequestDto());
		model.addAttribute("nombreUsuario", sesion.getNombre());

		try {
			model.addAttribute("yaConfigurada",
					recuperacionService.tieneConfigurada(sesion.getIdUsuario()));
		} catch (Exception ex) {
			model.addAttribute("yaConfigurada", false);
		}

		return "seguridad/pregunta";
	}

	@PostMapping("/pregunta")
	public String guardar(@ModelAttribute ConfigurarPreguntaRequestDto configuracion,
			HttpSession session, Model model) {

		LoginResponseDto sesion = (LoginResponseDto) session.getAttribute("usuarioActual");
		if (sesion == null) {
			return "redirect:/login";
		}

		model.addAttribute("preguntas", PREGUNTAS);
		model.addAttribute("nombreUsuario", sesion.getNombre());

		try {
			recuperacionService.configurar(sesion.getIdUsuario(), configuracion);
			return "redirect:/seguridad/pregunta?guardada=true";

		} catch (WebClientResponseException.Conflict ex) {
			model.addAttribute("error", ex.getResponseBodyAsString());
		} catch (WebClientResponseException.BadRequest ex) {
			model.addAttribute("error", "Debe elegir una pregunta y escribir una respuesta.");
		} catch (Exception ex) {
			model.addAttribute("error", "No se pudo conectar con el servidor. Intente nuevamente.");
		}

		model.addAttribute("configuracion", configuracion);
		model.addAttribute("yaConfigurada", false);
		return "seguridad/pregunta";
	}

}