package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.CargoRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CargoResponseDto;
import com.uisrael.prototipogestalabweb.services.ICargoService;

public class CargoServiceImpl implements ICargoService{
	
	private final WebClient webClient;

	public CargoServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<CargoResponseDto> listarCargos() {
		// TODO Auto-generated method stub
		return webClient.get().uri("/cargo/query").retrieve()
				.bodyToFlux(CargoResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarCargos(CargoRequestDto cargo) {
		// TODO Auto-generated method stub
		webClient.post().uri("/cargo")
		.bodyValue(cargo)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
