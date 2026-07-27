package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.InformacionMatrizPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.InformacionMatrizPLResponseDto;
import com.uisrael.prototipogestalabweb.services.IInformacionMatrizPLService;

@Service
public class InformacionMatrizPLServiceImpl implements IInformacionMatrizPLService {

	private final WebClient webClient;

	public InformacionMatrizPLServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<InformacionMatrizPLResponseDto> listarMatrices() {
		return webClient.get().uri("/gestalab/informacion-matriz-pl")
				.retrieve()
				.bodyToFlux(InformacionMatrizPLResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public InformacionMatrizPLResponseDto guardar(InformacionMatrizPLRequestDto dto) {
		return webClient.post().uri("/gestalab/informacion-matriz-pl")
				.bodyValue(dto)
				.retrieve()
				.bodyToMono(InformacionMatrizPLResponseDto.class)
				.block();
	}

	@Override
	public void eliminar(int idInfoMatriz) {
		webClient.delete().uri("/gestalab/informacion-matriz-pl/{id}", idInfoMatriz)
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	@Override
	public List<InformacionMatrizPLResponseDto> listarPorPlan(int idPlan) {
		return webClient.get().uri("/gestalab/informacion-matriz-pl/plan/{idPlan}", idPlan)
				.retrieve()
				.bodyToFlux(InformacionMatrizPLResponseDto.class)
				.collectList()
				.block();
	}

}
