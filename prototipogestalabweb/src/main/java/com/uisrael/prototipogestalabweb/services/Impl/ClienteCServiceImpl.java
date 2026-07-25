package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.ClienteCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.ClienteCResponseDto;
import com.uisrael.prototipogestalabweb.services.IClienteCService;

@Service
public class ClienteCServiceImpl implements IClienteCService{
	
	private final WebClient webClient;

	public ClienteCServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<ClienteCResponseDto> listarClientes() {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/cliente")
				.retrieve()
				.bodyToFlux(ClienteCResponseDto.class)
				.collectList()
				.block();
	}

	

	@Override
	public ClienteCResponseDto buscarPorId(int idClienteC) {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/cliente/{id}", idClienteC)
				.retrieve()
				.bodyToMono(ClienteCResponseDto.class)
				.block();
	}

	@Override
	public void eliminarCliente(int idClienteC) {
		// TODO Auto-generated method stub
		webClient.delete().uri("/gestalab/cliente/{id}", idClienteC)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

	@Override
	public ClienteCResponseDto guardarCliente(ClienteCRequestDto cliente) {
		// TODO Auto-generated method stub
		return webClient.post().uri("/gestalab/cliente")
				.bodyValue(cliente)
				.retrieve()
				.bodyToMono(ClienteCResponseDto.class)
				.block();
	}
	
	

}
