package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.LmpCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.LmpCResponseDto;
import com.uisrael.prototipogestalabweb.services.ILmpCService;

@Controller
@RequestMapping("/lmp")
public class LmpCController {
	
	private final ILmpCService service;

	public LmpCController(ILmpCService service) {
		super();
		this.service = service;
	}
	
	@GetMapping("/listar")
	public String listar(Model model) {
		List<LmpCResponseDto> items = service.listarLmpCs();
		model.addAttribute("items", items);
		return "lmp/listarlmp";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("item", new LmpCRequestDto());
		return "lmp/nuevolmp";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute LmpCRequestDto item) {
		service.guardarLmpC(item);
		return "redirect:/lmp/listar?success=true";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		try {
			LmpCResponseDto actual = service.buscarPorId(id);

			LmpCRequestDto form = new LmpCRequestDto();
			form.setIdLmpC(actual.getIdLmpC());
			form.setCodigoLmpC(actual.getCodigoLmpC());
			form.setValorLmpC(actual.getValorLmpC());

			model.addAttribute("item", form);
			model.addAttribute("esEdicion", true);
			return "lmp/editarlmp";
		} catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/actualizar/{id}")
	public String actualizar(@PathVariable int id, @ModelAttribute LmpCRequestDto item) {
		item.setIdLmpC(id);
		try {
			service.guardarLmpC(item);
			return "redirect:/lmp/listar?success=true";
		} catch (Exception e) {
			return "lmp/editarlmp";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable int id) {
		try {
			service.eliminarLmpC(id);
			return "redirect:/lmp/listar?deleted=true";
		} catch (Exception e) {
			return "redirect:/lmp/listar?error=true";
		}
	}

}
