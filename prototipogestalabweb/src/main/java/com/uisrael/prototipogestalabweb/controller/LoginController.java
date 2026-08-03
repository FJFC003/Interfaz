package com.uisrael.prototipogestalabweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.uisrael.prototipogestalabweb.model.dto.request.LoginRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.LoginResponseDto;
import com.uisrael.prototipogestalabweb.services.ILoginService;
import com.uisrael.prototipogestalabweb.services.IRecuperacionAccesoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	
	private final ILoginService loginService;
	private final IRecuperacionAccesoService recuperacionService;

	public LoginController(ILoginService loginService,
			IRecuperacionAccesoService recuperacionService) {
		super();
		this.loginService = loginService;
		this.recuperacionService = recuperacionService;
	}
	
	@GetMapping("/login")
	public String mostrarLogin(Model model) {
		model.addAttribute("credenciales", new LoginRequestDto());
		return "login";
	}

	@PostMapping("/login")
	public String procesarLogin(@ModelAttribute LoginRequestDto credenciales, HttpSession session, Model model) {
		try {
			LoginResponseDto usuario = loginService.login(credenciales);
			session.setAttribute("usuarioActual", usuario);

			String rol = usuario.getRol() == null ? "" : usuario.getRol().trim();

			// Gerente General -> administracion de empleados
			if (rol.equalsIgnoreCase("Gerente General")) {

				// Si todavia no tiene pregunta de seguridad, se le pide configurarla.
				// Sin esto, olvidar la contrasena la dejaria fuera del sistema.
				if (!tienePreguntaConfigurada(usuario.getIdUsuario())) {
					return "redirect:/seguridad/pregunta?primeraVez=true";
				}
				return "redirect:/empleado/listar";
			}

			// Coordinacion Comercial -> cotizaciones
			if (rol.equalsIgnoreCase("Coordinador Comercial")) {
				return "redirect:/cotizacion/listar";
			}

			// Coordinacion Tecnica -> planes de muestreo
			if (rol.equalsIgnoreCase("Coordinador Tecnico") || rol.equalsIgnoreCase("Coordinador Técnico")) {
				return "redirect:/plan/listar";
			}

			// Tecnico de Laboratorio -> bandeja de informes de resultados
			if (rol.equalsIgnoreCase("Tecnico de laboratorio") || rol.equalsIgnoreCase("Técnico de laboratorio")
					|| rol.equalsIgnoreCase("Laboratorista") || rol.equalsIgnoreCase("Analista")) {
				return "redirect:/informe/bandeja";
			}

			// Tecnico de Campo -> su bandeja de trabajos asignados
			if (rol.equalsIgnoreCase("Tecnico") || rol.equalsIgnoreCase("Técnico")
					|| rol.equalsIgnoreCase("Tecnico de campo") || rol.equalsIgnoreCase("Técnico de campo")) {
				return "redirect:/campo/mis-trabajos";
			}

			// Cualquier otro rol -> pantalla de inicio neutra
			return "redirect:/";

		} catch (WebClientResponseException ex) {
			model.addAttribute("error", "Correo o contraseña incorrectos, o el usuario no tiene un rol asignado.");
			model.addAttribute("credenciales", credenciales);
			return "login";
		} catch (Exception ex) {
			model.addAttribute("error", "No se pudo conectar con el servidor. Intente nuevamente.");
			model.addAttribute("credenciales", credenciales);
			return "login";
		}
	}

	/**
	 * Si la consulta falla por cualquier motivo se asume que si esta configurada,
	 * para no bloquear el acceso por un problema de red.
	 */
	private boolean tienePreguntaConfigurada(int idUsuario) {
		try {
			return recuperacionService.tieneConfigurada(idUsuario);
		} catch (Exception ex) {
			return true;
		}
	}

	@GetMapping("/logout")
	public String cerrarSesion(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
	
}
