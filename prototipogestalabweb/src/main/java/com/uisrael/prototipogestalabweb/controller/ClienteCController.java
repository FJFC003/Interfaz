package com.uisrael.prototipogestalabweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.prototipogestalabweb.model.dto.request.ClienteCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.ClienteCResponseDto;
import com.uisrael.prototipogestalabweb.services.IClienteCService;

@Controller
@RequestMapping("/cliente")
public class ClienteCController {
	
	private final IClienteCService clienteCService;

	public ClienteCController(IClienteCService clienteCService) {
		super();
		this.clienteCService = clienteCService;
	}
	
	@GetMapping("/listar")
	public String listarClientes(Model model) {
		List<ClienteCResponseDto> clientes = clienteCService.listarClientes();
		model.addAttribute("clientes", clientes);
		return "cliente/listarcliente";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("cliente", new ClienteCRequestDto());
		return "cliente/nuevocliente";
	}

	@PostMapping("/guardar")
	public String guardarCliente(@ModelAttribute ClienteCRequestDto cliente) {
		clienteCService.guardarCliente(cliente);
		return "redirect:/cliente/listar?success=true";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		try {
			ClienteCResponseDto clienteActual = clienteCService.buscarPorId(id);

			ClienteCRequestDto clienteForm = new ClienteCRequestDto();
			clienteForm.setIdClienteC(clienteActual.getIdClienteC());
			clienteForm.setTipoClienteC(clienteActual.getTipoClienteC());
			clienteForm.setCiClienteC(clienteActual.getCiClienteC());
			clienteForm.setNombreRazonSocialClienteC(clienteActual.getNombreRazonSocialClienteC());
			clienteForm.setNombrePersonaContactoClienteC(clienteActual.getNombrePersonaContactoClienteC());
			clienteForm.setDireccionClienteC(clienteActual.getDireccionClienteC());
			clienteForm.setTelefonoClienteC(clienteActual.getTelefonoClienteC());
			clienteForm.setCorreoClienteC(clienteActual.getCorreoClienteC());
			clienteForm.setEstadoClienteC(clienteActual.isEstadoClienteC());

			model.addAttribute("cliente", clienteForm);
			model.addAttribute("esEdicion", true);
			return "cliente/editarcliente";
		} catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/actualizar/{id}")
	public String actualizarCliente(@PathVariable int id, @ModelAttribute ClienteCRequestDto cliente) {
		cliente.setIdClienteC(id);
		try {
			clienteCService.guardarCliente(cliente);
			return "redirect:/cliente/listar?success=true";
		} catch (Exception e) {
			return "cliente/editarcliente";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarCliente(@PathVariable int id) {
		try {
			clienteCService.eliminarCliente(id);
			return "redirect:/cliente/listar?deleted=true";
		} catch (Exception e) {
			return "redirect:/cliente/listar?error=true";
		}
	}

}
