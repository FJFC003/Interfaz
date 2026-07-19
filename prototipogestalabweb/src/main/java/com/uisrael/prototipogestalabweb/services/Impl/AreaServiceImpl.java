package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.AreaRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.AreaResponseDto;
import com.uisrael.prototipogestalabweb.services.IAreaService;

public class AreaServiceImpl implements IAreaService{

	private final WebClient webClient;
	
	public AreaServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<AreaResponseDto> listarAreas() {
		// TODO Auto-generated method stub
		return webClient.get().uri("/area/query")
				.retrieve()
				.bodyToFlux(AreaResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarAreas(AreaRequestDto area) {
		// TODO Auto-generated method stub
		webClient.post().uri("/area").bodyValue(area)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
