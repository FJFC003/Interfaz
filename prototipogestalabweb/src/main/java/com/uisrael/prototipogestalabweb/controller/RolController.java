package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.RolRequestDto;
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
	
	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("rol", new RolRequestDto());
		return "rol/nuevorol";
	}

	@PostMapping("/guardar")
	public String guardarRol(@ModelAttribute RolRequestDto rol) {
		rolService.guardarRoles(rol);
		return "redirect:/rol/listar?success=true";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		try {
			RolResponseDto rolActual = rolService.buscarPorId(id);

			RolRequestDto rolForm = new RolRequestDto();
			rolForm.setIdRol(rolActual.getIdRol());
			rolForm.setNombre(rolActual.getNombre());
			rolForm.setDescripcion(rolActual.getDescripcion());
			rolForm.setEstadoRol(rolActual.isEstadoRol());

			model.addAttribute("rol", rolForm);
			model.addAttribute("esEdicion", true);
			return "rol/editarrol";
		} catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/actualizar/{id}")
	public String actualizarRol(
			@PathVariable int id,
			@ModelAttribute RolRequestDto rol) {
		rol.setIdRol(id);
		try {
			rolService.guardarRoles(rol);
			return "redirect:/rol/listar?success=true";
		} catch (Exception e) {
			return "rol/editarrol";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarRol(@PathVariable int id) {
		try {
			rolService.eliminarRol(id);
			return "redirect:/rol/listar?deleted=true";
		} catch (Exception e) {
			return "redirect:/rol/listar?error=true";
		}
	}

}
