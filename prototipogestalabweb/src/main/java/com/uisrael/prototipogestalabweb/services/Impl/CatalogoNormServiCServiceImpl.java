package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.CatalogoNormServiCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CatalogoNormServiCResponseDto;
import com.uisrael.prototipogestalabweb.services.ICatalogoNormServiCService;
@Service
public class CatalogoNormServiCServiceImpl implements ICatalogoNormServiCService{
	
	private final WebClient webClient;

	public CatalogoNormServiCServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<CatalogoNormServiCResponseDto> listarNormas() {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/catanormser")
				.retrieve()
				.bodyToFlux(CatalogoNormServiCResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarNorma(CatalogoNormServiCRequestDto norma) {
		// TODO Auto-generated method stub
		webClient.post().uri("/gestalab/catanormser")
		.bodyValue(norma)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

	@Override
	public CatalogoNormServiCResponseDto buscarPorId(int idCatalogoNormServi) {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/catanormser/{id}", idCatalogoNormServi)
				.retrieve()
				.bodyToMono(CatalogoNormServiCResponseDto.class)
				.block();
	}

	@Override
	public void eliminarNorma(int idCatalogoNormServi) {
		// TODO Auto-generated method stub
		webClient.delete().uri("/gestalab/catanormser/{id}", idCatalogoNormServi)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
