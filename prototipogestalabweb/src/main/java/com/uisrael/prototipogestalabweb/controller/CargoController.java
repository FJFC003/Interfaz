package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
            model.addAttribute("cargos", cargoBD);
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
       return "redirect:/cargo/listar?success=true";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        try {
            CargoResponseDto cargoActual = cargoService.buscarPorId(id);

            CargoRequestDto cargoForm = new CargoRequestDto();
            cargoForm.setIdCargo(cargoActual.getIdCargo());
            cargoForm.setNombre(cargoActual.getNombre());
            cargoForm.setDescripcion(cargoActual.getDescripcion());
            cargoForm.setEstadoCargo(cargoActual.isEstadoCargo());

            model.addAttribute("cargo", cargoForm);
            model.addAttribute("esEdicion", true);
            return "cargo/editarcargo";
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCargo(
            @PathVariable int id,
            @ModelAttribute CargoRequestDto cargo) {
        cargo.setIdCargo(id);
        try {
            cargoService.guardarCargos(cargo);
            return "redirect:/cargo/listar?success=true";
        } catch (Exception e) {
            return "cargo/editarcargo";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCargo(@PathVariable int id) {
        try {
            cargoService.eliminarCargo(id);
            return "redirect:/cargo/listar?deleted=true";
        } catch (Exception e) {
            return "redirect:/cargo/listar?error=true";
        }
    }


}
