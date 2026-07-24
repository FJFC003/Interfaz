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

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	
	private final ILoginService loginService;

	public LoginController(ILoginService loginService) {
		super();
		this.loginService = loginService;
	}
	
	@GetMapping("/login")
	public String mostrarLogin(Model model) {
		model.addAttribute("credenciales", new LoginRequestDto());
		return "login";
	}

	@PostMapping("/login")
	public String procesarLogin(
			@ModelAttribute LoginRequestDto credenciales,
			HttpSession session,
			Model model) {
		try {
			LoginResponseDto usuario = loginService.login(credenciales);
			session.setAttribute("usuarioActual", usuario);
			return "redirect:/empleado/listar";
		} catch (WebClientResponseException ex) {
			model.addAttribute("error", "Correo, contraseña incorrectos, o el usuario no tiene permisos de Gerente General.");
			model.addAttribute("credenciales", credenciales);
			return "login";
		} catch (Exception ex) {
			model.addAttribute("error", "No se pudo conectar con el servidor. Intente nuevamente.");
			model.addAttribute("credenciales", credenciales);
			return "login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}

}
