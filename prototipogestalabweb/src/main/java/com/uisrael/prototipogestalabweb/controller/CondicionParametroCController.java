package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.CondicionParametroCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CondicionParametroCResponseDto;
import com.uisrael.prototipogestalabweb.services.ICondicionParametroCService;


@Controller
@RequestMapping("/condicionparametro")
public class CondicionParametroCController {
	
	private final ICondicionParametroCService condicionService;

	public CondicionParametroCController(ICondicionParametroCService condicionService) {
		super();
		this.condicionService = condicionService;
	}
	
	@GetMapping("/listar")
	public String listarCondiciones(Model model) {
		List<CondicionParametroCResponseDto> condiciones = condicionService.listarCondiciones();
		model.addAttribute("condiciones", condiciones);
		return "condicionparametro/listarcondicion";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("condicion", new CondicionParametroCRequestDto());
		return "condicionparametro/nuevacondicion";
	}

	@PostMapping("/guardar")
	public String guardarCondicion(@ModelAttribute CondicionParametroCRequestDto condicion) {
		condicionService.guardarCondicion(condicion);
		return "redirect:/condicionparametro/listar?success=true";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		try {
			CondicionParametroCResponseDto actual = condicionService.buscarPorId(id);

			CondicionParametroCRequestDto form = new CondicionParametroCRequestDto();
			form.setIdCondicionParametroC(actual.getIdCondicionParametroC());
			form.setCodigoCondicionParametroC(actual.getCodigoCondicionParametroC());
			form.setDescripcionCondicionParametroC(actual.getDescripcionCondicionParametroC());

			model.addAttribute("condicion", form);
			model.addAttribute("esEdicion", true);
			return "condicionparametro/editarcondicion";
		} catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/actualizar/{id}")
	public String actualizarCondicion(@PathVariable int id, @ModelAttribute CondicionParametroCRequestDto condicion) {
		condicion.setIdCondicionParametroC(id);
		try {
			condicionService.guardarCondicion(condicion);
			return "redirect:/condicionparametro/listar?success=true";
		} catch (Exception e) {
			return "condicionparametro/editarcondicion";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarCondicion(@PathVariable int id) {
		try {
			condicionService.eliminarCondicion(id);
			return "redirect:/condicionparametro/listar?deleted=true";
		} catch (Exception e) {
			return "redirect:/condicionparametro/listar?error=true";
		}
	}
	
}
