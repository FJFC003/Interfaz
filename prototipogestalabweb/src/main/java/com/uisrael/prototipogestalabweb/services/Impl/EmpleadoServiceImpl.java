package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.EmpleadoRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EmpleadoResponseDto;
import com.uisrael.prototipogestalabweb.services.IEmpleadoService;

@Service
public class EmpleadoServiceImpl implements IEmpleadoService{
	
	private final WebClient webClient;

	public EmpleadoServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<EmpleadoResponseDto> listarEmpleados() {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/empleado")
				.retrieve()
				.bodyToFlux(EmpleadoResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarEmpleados(EmpleadoRequestDto empleado) {
		// TODO Auto-generated method stub
		webClient.post().uri("/gestalab/empleado")
		.bodyValue(empleado)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

	@Override
	public EmpleadoResponseDto buscarPorId(int idEmpleado) {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/empleado/{id}", idEmpleado)
				.retrieve()
				.bodyToMono(EmpleadoResponseDto.class)
				.block();
	}

	@Override
	public void eliminarEmpleado(int idEmpleado) {
		// TODO Auto-generated method stub
		webClient.delete().uri("/gestalab/empleado/{id}", idEmpleado)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
