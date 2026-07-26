package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.DescripcionServicioCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DescripcionServicioCResponseDto;
import com.uisrael.prototipogestalabweb.services.IDescripcionServicioCService;

@Service
public class DescripcionServicioCServiceImpl implements IDescripcionServicioCService{
	
	private final WebClient webClient;

	public DescripcionServicioCServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<DescripcionServicioCResponseDto> listarDescripcionServicioCs() {
		return webClient.get().uri("/gestalab/descripcionservicio")
				.retrieve()
				.bodyToFlux(DescripcionServicioCResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarDescripcionServicioC(DescripcionServicioCRequestDto item) {
		webClient.post().uri("/gestalab/descripcionservicio")
		.bodyValue(item)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

	@Override
	public DescripcionServicioCResponseDto buscarPorId(int idDescripcionServicioC) {
		return webClient.get().uri("/gestalab/descripcionservicio/{id}", idDescripcionServicioC)
				.retrieve()
				.bodyToMono(DescripcionServicioCResponseDto.class)
				.block();
	}

	@Override
	public void eliminarDescripcionServicioC(int idDescripcionServicioC) {
		webClient.delete().uri("/gestalab/descripcionservicio/{id}", idDescripcionServicioC)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
