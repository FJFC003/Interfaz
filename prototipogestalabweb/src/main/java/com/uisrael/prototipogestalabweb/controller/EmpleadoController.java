package com.uisrael.prototipogestalabweb.controller;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.EmpleadoRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.AreaResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CargoResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EmpleadoResponseDto;
import com.uisrael.prototipogestalabweb.services.IAreaService;
import com.uisrael.prototipogestalabweb.services.ICargoService;
import com.uisrael.prototipogestalabweb.services.IEmpleadoService;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {
	
	private final IEmpleadoService empleadoService;
    private final IAreaService areaService;
    private final ICargoService cargoService;

    public EmpleadoController(IEmpleadoService empleadoService, IAreaService areaService, ICargoService cargoService) {
		super();
		this.empleadoService = empleadoService;
		this.areaService = areaService;
		this.cargoService = cargoService;
	}

	// Display list of employees
    @GetMapping("/listar")
    public String listarEmpleados(Model model) {
            List<EmpleadoResponseDto> empleadosBD = empleadoService.listarEmpleados();
            model.addAttribute("empleados", empleadosBD);
            return "/empleado/listarempleado";
    }

    // Show create employee form
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
            List<AreaResponseDto> areas = areaService.listarAreas();
            List<CargoResponseDto> cargos = cargoService.listarCargos();
            
            model.addAttribute("empleado", new EmpleadoRequestDto());
            model.addAttribute("areas", areas);
            model.addAttribute("cargos", cargos);
            
            return "empleado/nuevoempleado";
    }

    // Save new employee
    @PostMapping("/guardar")
    public String guardarEmpleado(@ModelAttribute EmpleadoRequestDto empleado) {
        
       empleado.setFechaIngreso(new Date());
       empleadoService.guardarEmpleados(empleado);
       return "redirect:/empleado";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        try {
            List<AreaResponseDto> areas = areaService.listarAreas();
            List<CargoResponseDto> cargos = cargoService.listarCargos();
            
            model.addAttribute("empleado", new EmpleadoRequestDto());
            model.addAttribute("areas", areas);
            model.addAttribute("cargos", cargos);
            model.addAttribute("esEdicion", true);
            return "empleado/editarempleado";
        } catch (Exception e) {
            return "error";
        }
    }
    
    @PostMapping("/actualizar/{id}")
    public String actualizarEmpleado(
            @PathVariable int id,
            @ModelAttribute EmpleadoRequestDto empleado,
            Model model) {
        try {
            empleadoService.guardarEmpleados(empleado);
            return "redirect:/empleado/listar?success=true";
        } catch (Exception e) {
            return "empleado/editarempleado";
        }
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable int id) {
        try {
            return "redirect:/empleado/listar?deleted=true";
        } catch (Exception e) {
            return "redirect:/empleado/listar?error=true";
        }
    }
}
