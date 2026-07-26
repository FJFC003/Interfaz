package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.LmpCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.LmpCResponseDto;
import com.uisrael.prototipogestalabweb.services.ILmpCService;

@Service
public class LmpCServiceImpl implements ILmpCService{
	
	private final WebClient webClient;

	public LmpCServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<LmpCResponseDto> listarLmpCs() {
		return webClient.get().uri("/gestalab/lmp")
				.retrieve()
				.bodyToFlux(LmpCResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarLmpC(LmpCRequestDto item) {
		webClient.post().uri("/gestalab/lmp")
		.bodyValue(item)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

	@Override
	public LmpCResponseDto buscarPorId(int idLmpC) {
		return webClient.get().uri("/gestalab/lmp/{id}", idLmpC)
				.retrieve()
				.bodyToMono(LmpCResponseDto.class)
				.block();
	}

	@Override
	public void eliminarLmpC(int idLmpC) {
		webClient.delete().uri("/gestalab/lmp/{id}", idLmpC)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
