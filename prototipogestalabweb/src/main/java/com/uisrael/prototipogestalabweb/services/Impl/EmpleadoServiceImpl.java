package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.EmpleadoRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EmpleadoResponseDto;
import com.uisrael.prototipogestalabweb.services.IEmpleadoService;

public class EmpleadoServiceImpl implements IEmpleadoService{
	
	private final WebClient webClient;

	public EmpleadoServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<EmpleadoResponseDto> listarEmpleados() {
		// TODO Auto-generated method stub
		return webClient.get().uri("/api/gestalab/empleado")
				.retrieve()
				.bodyToFlux(EmpleadoResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarEmpleados(EmpleadoRequestDto empleado) {
		// TODO Auto-generated method stub
		webClient.post().uri("/api/gestalab/empleado")
		.bodyValue(empleado)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
