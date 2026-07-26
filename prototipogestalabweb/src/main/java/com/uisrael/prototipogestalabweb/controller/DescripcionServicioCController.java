package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.DescripcionServicioCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DescripcionServicioCResponseDto;
import com.uisrael.prototipogestalabweb.services.IDescripcionServicioCService;

@Controller
@RequestMapping("/descripcionservicio")
public class DescripcionServicioCController {
	
	private final IDescripcionServicioCService service;

	public DescripcionServicioCController(IDescripcionServicioCService service) {
		super();
		this.service = service;
	}
	
	@GetMapping("/listar")
	public String listar(Model model) {
		List<DescripcionServicioCResponseDto> items = service.listarDescripcionServicioCs();
		model.addAttribute("items", items);
		return "descripcionservicio/listardescripcionservicio";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("item", new DescripcionServicioCRequestDto());
		return "descripcionservicio/nuevodescripcionservicio";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute DescripcionServicioCRequestDto item) {
		service.guardarDescripcionServicioC(item);
		return "redirect:/descripcionservicio/listar?success=true";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		try {
			DescripcionServicioCResponseDto actual = service.buscarPorId(id);

			DescripcionServicioCRequestDto form = new DescripcionServicioCRequestDto();
			form.setIdDescripcionServicioC(actual.getIdDescripcionServicioC());
			form.setCodigoDescripcionServicioC(actual.getCodigoDescripcionServicioC());
			form.setTextoDescripcionServicioC(actual.getTextoDescripcionServicioC());

			model.addAttribute("item", form);
			model.addAttribute("esEdicion", true);
			return "descripcionservicio/editardescripcionservicio";
		} catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/actualizar/{id}")
	public String actualizar(@PathVariable int id, @ModelAttribute DescripcionServicioCRequestDto item) {
		item.setIdDescripcionServicioC(id);
		try {
			service.guardarDescripcionServicioC(item);
			return "redirect:/descripcionservicio/listar?success=true";
		} catch (Exception e) {
			return "descripcionservicio/editardescripcionservicio";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable int id) {
		try {
			service.eliminarDescripcionServicioC(id);
			return "redirect:/descripcionservicio/listar?deleted=true";
		} catch (Exception e) {
			return "redirect:/descripcionservicio/listar?error=true";
		}
	}

}
