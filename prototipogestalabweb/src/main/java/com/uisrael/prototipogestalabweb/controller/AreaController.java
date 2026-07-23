package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.AreaRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.AreaResponseDto;
import com.uisrael.prototipogestalabweb.services.IAreaService;

@Controller
@RequestMapping("/area")
public class AreaController {
	
	private final IAreaService areaService;

	public AreaController(IAreaService areaService) {
		super();
		this.areaService = areaService;
	}
	
	// Display list of employees
    @GetMapping("/listar")
    public String listarAreas(Model model) {
            List<AreaResponseDto> areaBD = areaService.listarAreas();
            model.addAttribute("areas", areaBD);
            return "/area/listararea";
    }

 // Show create employee form
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
            model.addAttribute("area", new AreaRequestDto());
            
            return "area/nuevoarea";
    }

    // Save new employee
    @PostMapping("/guardar")
    public String guardarArea(@ModelAttribute AreaRequestDto area) {
        
       areaService.guardarAreas(area);
       return "redirect:/area/listar?success=true";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
    	try {
            AreaResponseDto areaActual = areaService.buscarPorId(id);

            AreaRequestDto areaForm = new AreaRequestDto();
            areaForm.setIdArea(areaActual.getIdArea());
            areaForm.setNombre(areaActual.getNombre());
            areaForm.setDescripcion(areaActual.getDescripcion());
            areaForm.setEstadoArea(areaActual.isEstadoArea());

            model.addAttribute("area", areaForm);
            model.addAttribute("esEdicion", true);
            return "area/editararea";
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarArea(
            @PathVariable int id,
            @ModelAttribute AreaRequestDto area) {
        area.setIdArea(id);
    	try {
            areaService.guardarAreas(area);
            return "redirect:/area/listar?success=true";
        } catch (Exception e) {
            return "area/editararea";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarArea(@PathVariable int id) {
        try {
        	areaService.eliminarArea(id);
            return "redirect:/area/listar?deleted=true";
        } catch (Exception e) {
            return "redirect:/area/listar?error=true";
        }
    }

}
