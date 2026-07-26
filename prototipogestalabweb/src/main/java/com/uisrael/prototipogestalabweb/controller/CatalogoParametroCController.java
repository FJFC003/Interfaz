package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.CatalogoParametroCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CatalogoParametroCResponseDto;
import com.uisrael.prototipogestalabweb.services.ICatalogoParametroCService;

@Controller
@RequestMapping("/parametro")
public class CatalogoParametroCController {
	
	private final ICatalogoParametroCService parametroCService;

	public CatalogoParametroCController(ICatalogoParametroCService parametroCService) {
		super();
		this.parametroCService = parametroCService;
	}

	@GetMapping("/listar")
	public String listarParametros(Model model) {
		List<CatalogoParametroCResponseDto> parametros = parametroCService.listarParametros();
		model.addAttribute("parametros", parametros);
		return "parametro/listarparametro";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("parametro", new CatalogoParametroCRequestDto());
		return "parametro/nuevoparametro";
	}

	@PostMapping("/guardar")
	public String guardarParametro(@ModelAttribute CatalogoParametroCRequestDto parametro) {
		parametroCService.guardarParametro(parametro);
		return "redirect:/parametro/listar?success=true";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		try {
			CatalogoParametroCResponseDto actual = parametroCService.buscarPorId(id);

			CatalogoParametroCRequestDto form = new CatalogoParametroCRequestDto();
			form.setIdParametroC(actual.getIdParametroC());
			form.setEnsayoParametroC(actual.getEnsayoParametroC());
			form.setTecnicaParametroC(actual.getTecnicaParametroC());
			form.setProcedimientoInternoParametroC(actual.getProcedimientoInternoParametroC());
			form.setNormaReferencialParametroC(actual.getNormaReferencialParametroC());
			form.setUnidadParametroC(actual.getUnidadParametroC());
			form.setRangoTrabajoParametroC(actual.getRangoTrabajoParametroC());

			model.addAttribute("parametro", form);
			model.addAttribute("esEdicion", true);
			return "parametro/editarparametro";
		} catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/actualizar/{id}")
	public String actualizarParametro(@PathVariable int id, @ModelAttribute CatalogoParametroCRequestDto parametro) {
		parametro.setIdParametroC(id);
		try {
			parametroCService.guardarParametro(parametro);
			return "redirect:/parametro/listar?success=true";
		} catch (Exception e) {
			return "parametro/editarparametro";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarParametro(@PathVariable int id) {
		try {
			parametroCService.eliminarParametro(id);
			return "redirect:/parametro/listar?deleted=true";
		} catch (Exception e) {
			return "redirect:/parametro/listar?error=true";
		}
	}
	
}
