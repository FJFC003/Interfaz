package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.PlazoEntregaCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.PlazoEntregaCResponseDto;
import com.uisrael.prototipogestalabweb.services.IPlazoEntregaCService;

@Service
public class PlazoEntregaCServiceImpl implements IPlazoEntregaCService{
	
	private final WebClient webClient;

	public PlazoEntregaCServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<PlazoEntregaCResponseDto> listarPlazoEntregaCs() {
		return webClient.get().uri("/gestalab/plazoentrega")
				.retrieve()
				.bodyToFlux(PlazoEntregaCResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarPlazoEntregaC(PlazoEntregaCRequestDto item) {
		webClient.post().uri("/gestalab/plazoentrega")
		.bodyValue(item)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

	@Override
	public PlazoEntregaCResponseDto buscarPorId(int idPlazoEntregaC) {
		return webClient.get().uri("/gestalab/plazoentrega/{id}", idPlazoEntregaC)
				.retrieve()
				.bodyToMono(PlazoEntregaCResponseDto.class)
				.block();
	}

	@Override
	public void eliminarPlazoEntregaC(int idPlazoEntregaC) {
		webClient.delete().uri("/gestalab/plazoentrega/{id}", idPlazoEntregaC)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
