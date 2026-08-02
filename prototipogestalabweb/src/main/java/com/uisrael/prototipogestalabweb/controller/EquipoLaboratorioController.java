package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.EquipoLaboratorioRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EquipoLaboratorioResponseDto;
import com.uisrael.prototipogestalabweb.services.IEquipoLaboratorioService;

@Controller
@RequestMapping("/equipolaboratorio")
public class EquipoLaboratorioController {

	private final IEquipoLaboratorioService service;

	public EquipoLaboratorioController(IEquipoLaboratorioService service) {
		super();
		this.service = service;
	}

	@GetMapping("/listar")
	public String listar(Model model) {
		List<EquipoLaboratorioResponseDto> items = service.listarEquipos();
		model.addAttribute("items", items);
		return "equipo/listarequipo";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("item", new EquipoLaboratorioRequestDto());
		return "equipo/nuevoequipo";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute EquipoLaboratorioRequestDto item) {
		service.guardarEquipo(item);
		return "redirect:/equipolaboratorio/listar?success=true";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		try {
			EquipoLaboratorioResponseDto actual = service.buscarPorId(id);

			EquipoLaboratorioRequestDto form = new EquipoLaboratorioRequestDto();
			form.setIdEquipoLab(actual.getIdEquipoLab());
			form.setNombre(actual.getNombre());
			form.setMarca(actual.getMarca());
			form.setModelo(actual.getModelo());
			form.setSerie(actual.getSerie());
			form.setCodigoInterno(actual.getCodigoInterno());
			form.setEstadoEquipoLab(actual.isEstadoEquipoLab());

			model.addAttribute("item", form);
			model.addAttribute("esEdicion", true);
			return "equipo/editarequipo";
		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	@PostMapping("/actualizar/{id}")
	public String actualizar(@PathVariable int id, @ModelAttribute EquipoLaboratorioRequestDto item) {
		item.setIdEquipoLab(id);
		try {
			service.guardarEquipo(item);
			return "redirect:/equipolaboratorio/listar?success=true";
		} catch (Exception e) {
			return "equipo/editarequipo";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable int id) {
		try {
			service.eliminarEquipo(id);
			return "redirect:/equipolaboratorio/listar?deleted=true";
		} catch (Exception e) {
			// Un equipo ya usado en informes no se puede borrar: se da de baja.
			return "redirect:/equipolaboratorio/listar?error=true";
		}
	}

}