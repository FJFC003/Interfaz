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
import com.uisrael.prototipogestalabweb.model.dto.request.NormaParametroLmpCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CatalogoNormServiCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CatalogoParametroCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.LmpCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.NormaParametroLmpCResponseDto;
import com.uisrael.prototipogestalabweb.services.ICatalogoNormServiCService;
import com.uisrael.prototipogestalabweb.services.ICatalogoParametroCService;
import com.uisrael.prototipogestalabweb.services.ILmpCService;
import com.uisrael.prototipogestalabweb.services.INormaParametroLmpCService;

@Controller
@RequestMapping("/norma")
public class CatalogoNormServiCController {
	
	private final ICatalogoNormServiCService normService;
	private final INormaParametroLmpCService asociacionService;
	private final ICatalogoParametroCService parametroService;
	private final ILmpCService lmpService;
	
	public CatalogoNormServiCController(ICatalogoNormServiCService normService,
			INormaParametroLmpCService asociacionService, ICatalogoParametroCService parametroService,
			ILmpCService lmpService) {
		super();
		this.normService = normService;
		this.asociacionService = asociacionService;
		this.parametroService = parametroService;
		this.lmpService = lmpService;
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

	// Pantalla para asociar/desasociar pares Parámetro-LMP de una norma.
	@GetMapping("/detalle/{id}")
	public String verDetalle(@PathVariable int id, Model model) {
		try {
			CatalogoNormServiCResponseDto norma = normService.buscarPorId(id);
			List<NormaParametroLmpCResponseDto> pares = asociacionService.listarPorNorma(id);
			List<CatalogoParametroCResponseDto> parametros = parametroService.listarParametros();
			List<LmpCResponseDto> lmps = lmpService.listarLmpCs();

			model.addAttribute("norma", norma);
			model.addAttribute("pares", pares);
			model.addAttribute("parametros", parametros);
			model.addAttribute("lmps", lmps);
			model.addAttribute("nuevaAsociacion", new NormaParametroLmpCRequestDto());

			return "norma/detallenorma";
		} catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/detalle/asociar/{idNorma}")
	public String asociarPar(@PathVariable int idNorma, @ModelAttribute NormaParametroLmpCRequestDto asociacion) {
		asociacion.setFkNorma(idNorma);
		asociacionService.guardarAsociacion(asociacion);
		return "redirect:/norma/detalle/" + idNorma + "?success=true";
	}

	@GetMapping("/detalle/eliminar/{idPar}/{idNorma}")
	public String eliminarPar(@PathVariable int idPar, @PathVariable int idNorma) {
		try {
			asociacionService.eliminarAsociacion(idPar);
			return "redirect:/norma/detalle/" + idNorma + "?deleted=true";
		} catch (Exception e) {
			return "redirect:/norma/detalle/" + idNorma + "?error=true";
		}
	}

}
