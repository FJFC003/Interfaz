package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.CatalogoNormServiCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CatalogoNormServiCResponseDto;
import com.uisrael.prototipogestalabweb.services.ICatalogoNormServiCService;

@Controller
@RequestMapping("/norma")
public class CatalogoNormServiCController {
	
	private final ICatalogoNormServiCService normService;

	public CatalogoNormServiCController(ICatalogoNormServiCService normService) {
		super();
		this.normService = normService;
	}

	@GetMapping("/listar")
	public String listarNormas(Model model) {
		List<CatalogoNormServiCResponseDto> normas = normService.listarNormas();
		model.addAttribute("normas", normas);
		return "norma/listarnorma";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("norma", new CatalogoNormServiCRequestDto());
		return "norma/nuevanorma";
	}

	@PostMapping("/guardar")
	public String guardarNorma(@ModelAttribute CatalogoNormServiCRequestDto norma) {
		normService.guardarNorma(norma);
		return "redirect:/norma/listar?success=true";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		try {
			CatalogoNormServiCResponseDto actual = normService.buscarPorId(id);

			CatalogoNormServiCRequestDto form = new CatalogoNormServiCRequestDto();
			form.setIdCatalogoNormServi(actual.getIdCatalogoNormServi());
			form.setNombreCatalogoNormServiEntity(actual.getNombreCatalogoNormServiEntity());

			model.addAttribute("norma", form);
			model.addAttribute("esEdicion", true);
			return "norma/editarnorma";
		} catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/actualizar/{id}")
	public String actualizarNorma(@PathVariable int id, @ModelAttribute CatalogoNormServiCRequestDto norma) {
		norma.setIdCatalogoNormServi(id);
		try {
			normService.guardarNorma(norma);
			return "redirect:/norma/listar?success=true";
		} catch (Exception e) {
			return "norma/editarnorma";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarNorma(@PathVariable int id) {
		try {
			normService.eliminarNorma(id);
			return "redirect:/norma/listar?deleted=true";
		} catch (Exception e) {
			return "redirect:/norma/listar?error=true";
		}
	}

}