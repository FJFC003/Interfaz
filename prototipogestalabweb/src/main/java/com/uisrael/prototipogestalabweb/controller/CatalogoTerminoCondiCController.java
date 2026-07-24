package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.CatalogoTerminoCondiCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CatalogoTerminoCondiCResponseDto;
import com.uisrael.prototipogestalabweb.services.ICatalogoTerminoCondiCService;

@Controller
@RequestMapping("/termino")
public class CatalogoTerminoCondiCController {
	
	private final ICatalogoTerminoCondiCService terminoService;

	public CatalogoTerminoCondiCController(ICatalogoTerminoCondiCService terminoService) {
		super();
		this.terminoService = terminoService;
	}
	
	@GetMapping("/listar")
	public String listarTerminos(Model model) {
		List<CatalogoTerminoCondiCResponseDto> terminos = terminoService.listarTerminos();
		model.addAttribute("terminos", terminos);
		return "termino/listartermino";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("termino", new CatalogoTerminoCondiCRequestDto());
		return "termino/nuevotermino";
	}

	@PostMapping("/guardar")
	public String guardarTermino(@ModelAttribute CatalogoTerminoCondiCRequestDto termino) {
		terminoService.guardarTermino(termino);
		return "redirect:/termino/listar?success=true";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		try {
			CatalogoTerminoCondiCResponseDto actual = terminoService.buscarPorId(id);

			CatalogoTerminoCondiCRequestDto form = new CatalogoTerminoCondiCRequestDto();
			form.setIdTerminoC(actual.getIdTerminoC());
			form.setTituloTerminoC(actual.getTituloTerminoC());
			form.setContenidoTerminoC(actual.getContenidoTerminoC());

			model.addAttribute("termino", form);
			model.addAttribute("esEdicion", true);
			return "termino/editartermino";
		} catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/actualizar/{id}")
	public String actualizarTermino(@PathVariable int id, @ModelAttribute CatalogoTerminoCondiCRequestDto termino) {
		termino.setIdTerminoC(id);
		try {
			terminoService.guardarTermino(termino);
			return "redirect:/termino/listar?success=true";
		} catch (Exception e) {
			return "termino/editartermino";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarTermino(@PathVariable int id) {
		try {
			terminoService.eliminarTermino(id);
			return "redirect:/termino/listar?deleted=true";
		} catch (Exception e) {
			return "redirect:/termino/listar?error=true";
		}
	}

}
