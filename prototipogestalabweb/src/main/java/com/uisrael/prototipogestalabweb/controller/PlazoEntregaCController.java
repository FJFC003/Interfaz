package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.PlazoEntregaCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.PlazoEntregaCResponseDto;
import com.uisrael.prototipogestalabweb.services.IPlazoEntregaCService;

@Controller
@RequestMapping("/plazoentrega")
public class PlazoEntregaCController {
	
	private final IPlazoEntregaCService service;

	public PlazoEntregaCController(IPlazoEntregaCService service) {
		super();
		this.service = service;
	}
	
	@GetMapping("/listar")
	public String listar(Model model) {
		List<PlazoEntregaCResponseDto> items = service.listarPlazoEntregaCs();
		model.addAttribute("items", items);
		return "plazoentrega/listarplazoentrega";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("item", new PlazoEntregaCRequestDto());
		return "plazoentrega/nuevoplazoentrega";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute PlazoEntregaCRequestDto item) {
		service.guardarPlazoEntregaC(item);
		return "redirect:/plazoentrega/listar?success=true";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		try {
			PlazoEntregaCResponseDto actual = service.buscarPorId(id);

			PlazoEntregaCRequestDto form = new PlazoEntregaCRequestDto();
			form.setIdPlazoEntregaC(actual.getIdPlazoEntregaC());
			form.setCodigoPlazoEntregaC(actual.getCodigoPlazoEntregaC());
			form.setTextoPlazoEntregaC(actual.getTextoPlazoEntregaC());

			model.addAttribute("item", form);
			model.addAttribute("esEdicion", true);
			return "plazoentrega/editarplazoentrega";
		} catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/actualizar/{id}")
	public String actualizar(@PathVariable int id, @ModelAttribute PlazoEntregaCRequestDto item) {
		item.setIdPlazoEntregaC(id);
		try {
			service.guardarPlazoEntregaC(item);
			return "redirect:/plazoentrega/listar?success=true";
		} catch (Exception e) {
			return "plazoentrega/editarplazoentrega";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable int id) {
		try {
			service.eliminarPlazoEntregaC(id);
			return "redirect:/plazoentrega/listar?deleted=true";
		} catch (Exception e) {
			return "redirect:/plazoentrega/listar?error=true";
		}
	}

}
