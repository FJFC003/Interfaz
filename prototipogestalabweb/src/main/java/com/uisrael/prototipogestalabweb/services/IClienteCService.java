package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.ClienteCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.ClienteCResponseDto;

public interface IClienteCService {
	
	List<ClienteCResponseDto> listarClientes();
	void guardarCliente(ClienteCRequestDto cliente);
	ClienteCResponseDto buscarPorId(int idClienteC);
	void eliminarCliente(int idClienteC);

}
