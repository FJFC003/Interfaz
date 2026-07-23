package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.response.RolResponseDto;
import com.uisrael.prototipogestalabweb.services.IRolService;

@Controller
@RequestMapping("/rol")
public class RolController {
	
	private final IRolService rolService;

	public RolController(IRolService rolService) {
		super();
		this.rolService = rolService;
	}

	@GetMapping("/listar")
	public String listarRoles(Model model) {
		List<RolResponseDto> roles = rolService.listarRoles();
		model.addAttribute("roles", roles);
		return "rol/listarrol";
	}

}
