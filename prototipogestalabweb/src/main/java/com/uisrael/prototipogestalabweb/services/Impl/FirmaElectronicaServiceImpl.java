package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.FirmaElectronicaRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.FirmaElectronicaResponseDto;
import com.uisrael.prototipogestalabweb.services.IFirmaElectronicaService;

public class FirmaElectronicaServiceImpl implements IFirmaElectronicaService{
	
	private final WebClient webClient;

	public FirmaElectronicaServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<FirmaElectronicaResponseDto> listarFirmas() {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/firmaelectronica")
				.retrieve()
				.bodyToFlux(FirmaElectronicaResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarFirmas(FirmaElectronicaRequestDto firma) {
		// TODO Auto-generated method stub
		webClient.post().uri("/gestalab/firmaelectronica")
		.bodyValue(firma)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
