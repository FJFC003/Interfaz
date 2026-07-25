package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.CondicionParametroCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CondicionParametroCResponseDto;
import com.uisrael.prototipogestalabweb.services.ICondicionParametroCService;

@Service
public class CondicionParametroCServiceImpl implements ICondicionParametroCService{
	
	private final WebClient webClient;

	public CondicionParametroCServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<CondicionParametroCResponseDto> listarCondiciones() {
		return webClient.get().uri("/gestalab/condicionparametro")
				.retrieve()
				.bodyToFlux(CondicionParametroCResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarCondicion(CondicionParametroCRequestDto condicion) {
		// TODO Auto-generated method stub
		webClient.post().uri("/gestalab/condicionparametro")
		.bodyValue(condicion)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

	@Override
	public CondicionParametroCResponseDto buscarPorId(int idCondicionParametroC) {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/condicionparametro/{id}", idCondicionParametroC)
				.retrieve()
				.bodyToMono(CondicionParametroCResponseDto.class)
				.block();
	}

	@Override
	public void eliminarCondicion(int idCondicionParametroC) {
		// TODO Auto-generated method stub
		webClient.delete().uri("/gestalab/condicionparametro/{id}", idCondicionParametroC)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
