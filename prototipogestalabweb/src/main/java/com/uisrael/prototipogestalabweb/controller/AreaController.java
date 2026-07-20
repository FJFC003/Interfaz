package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
            model.addAttribute("area", areaBD);
            return "/area/listararea";
    }

    // Show create employee form
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
            model.addAttribute("areas", new AreaRequestDto());
            
            return "area/nuevoarea";
    }

    // Save new employee
    @PostMapping("/guardar")
    public String guardarArea(@ModelAttribute AreaRequestDto area) {
        
       areaService.guardarAreas(area);
       return "redirect:/area";
    }

}
