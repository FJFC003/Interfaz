package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.CargoRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CargoResponseDto;
import com.uisrael.prototipogestalabweb.services.ICargoService;

@Controller
@RequestMapping("/cargo")
public class CargoController {
	
	private final ICargoService cargoService;

	public CargoController(ICargoService cargoService) {
		super();
		this.cargoService = cargoService;
	}
	
	// Display list of employees
    @GetMapping("/listar")
    public String listarCargos(Model model) {
            List<CargoResponseDto> cargoBD = cargoService.listarCargos();
            model.addAttribute("cargo", cargoBD);
            return "/cargo/listarcargo";
    }

    // Show create employee form
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
            model.addAttribute("cargo", new CargoRequestDto());
            
            return "cargo/nuevocargo";
    }

    // Save new employee
    @PostMapping("/guardar")
    public String guardarCargo(@ModelAttribute CargoRequestDto cargo) {
        
       cargoService.guardarCargos(cargo);
       return "redirect:/cargo";
    }


}
