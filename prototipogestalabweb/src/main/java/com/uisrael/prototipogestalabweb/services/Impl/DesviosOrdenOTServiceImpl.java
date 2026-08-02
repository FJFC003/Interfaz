package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.DesviosOrdenOTRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DesviosOrdenOTResponseDto;
import com.uisrael.prototipogestalabweb.services.IDesviosOrdenOTService;

@Service
public class DesviosOrdenOTServiceImpl implements IDesviosOrdenOTService {

	private final WebClient webClient;

	public DesviosOrdenOTServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public DesviosOrdenOTResponseDto guardar(DesviosOrdenOTRequestDto dto) {
		return webClient.post().uri("/gestalab/desviosdeorden")
				.bodyValue(dto).retrieve().bodyToMono(DesviosOrdenOTResponseDto.class).block();
	}

	@Override
	public void eliminar(int idDesviosOrdenOT) {
		webClient.delete().uri("/gestalab/desviosdeorden/{id}", idDesviosOrdenOT)
				.retrieve().toBodilessEntity().block();
	}

	@Override
	public List<DesviosOrdenOTResponseDto> listarPorOrden(int idOT) {
		return webClient.get().uri("/gestalab/desviosdeorden/orden/{idOT}", idOT)
				.retrieve().bodyToFlux(DesviosOrdenOTResponseDto.class).collectList().block();
	}

}
