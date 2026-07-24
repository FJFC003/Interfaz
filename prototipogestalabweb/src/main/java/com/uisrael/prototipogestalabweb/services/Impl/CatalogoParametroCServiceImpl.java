package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.CatalogoParametroCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CatalogoParametroCResponseDto;
import com.uisrael.prototipogestalabweb.services.ICatalogoParametroCService;
@Service
public class CatalogoParametroCServiceImpl implements ICatalogoParametroCService{
	
	private final WebClient webClient;

	public CatalogoParametroCServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<CatalogoParametroCResponseDto> listarParametros() {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/cataparametro")
				.retrieve()
				.bodyToFlux(CatalogoParametroCResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarParametro(CatalogoParametroCRequestDto parametro) {
		// TODO Auto-generated method stub
		webClient.post().uri("/gestalab/cataparametro")
		.bodyValue(parametro)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

	@Override
	public CatalogoParametroCResponseDto buscarPorId(int idParametroC) {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/cataparametro/{id}", idParametroC)
		.retrieve()
		.bodyToMono(CatalogoParametroCResponseDto.class)
		.block();
	}

	@Override
	public void eliminarParametro(int idParametroC) {
		// TODO Auto-generated method stub
		webClient.delete().uri("/gestalab/cataparametro/{id}", idParametroC)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
