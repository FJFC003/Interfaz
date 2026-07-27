package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.ParametroAnalizarPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.ParametroAnalizarPLResponseDto;
import com.uisrael.prototipogestalabweb.services.IParametroAnalizarPLService;

@Service
public class ParametroAnalizarPLServiceImpl implements IParametroAnalizarPLService {

	private final WebClient webClient;

	public ParametroAnalizarPLServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<ParametroAnalizarPLResponseDto> listarParametros() {
		return webClient.get().uri("/gestalab/parametro-analizar-pl")
				.retrieve()
				.bodyToFlux(ParametroAnalizarPLResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public ParametroAnalizarPLResponseDto guardar(ParametroAnalizarPLRequestDto dto) {
		return webClient.post().uri("/gestalab/parametro-analizar-pl")
				.bodyValue(dto)
				.retrieve()
				.bodyToMono(ParametroAnalizarPLResponseDto.class)
				.block();
	}

	@Override
	public void eliminar(int idParametroPL) {
		webClient.delete().uri("/gestalab/parametro-analizar-pl/{id}", idParametroPL)
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	@Override
	public List<ParametroAnalizarPLResponseDto> listarPorPlan(int idPlan) {
		return webClient.get().uri("/gestalab/parametro-analizar-pl/plan/{idPlan}", idPlan)
				.retrieve()
				.bodyToFlux(ParametroAnalizarPLResponseDto.class)
				.collectList()
				.block();
	}

}
