package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.InformacionAdicionalPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.InformacionAdicionalPLResponseDto;
import com.uisrael.prototipogestalabweb.services.IInformacionAdicionalPLService;

@Service
public class InformacionAdicionalPLServiceImpl implements IInformacionAdicionalPLService {

	private final WebClient webClient;

	public InformacionAdicionalPLServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<InformacionAdicionalPLResponseDto> listarInformacionAdicional() {
		return webClient.get().uri("/gestalab/informacion-adicional-pl")
				.retrieve()
				.bodyToFlux(InformacionAdicionalPLResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public InformacionAdicionalPLResponseDto guardar(InformacionAdicionalPLRequestDto dto) {
		return webClient.post().uri("/gestalab/informacion-adicional-pl")
				.bodyValue(dto)
				.retrieve()
				.bodyToMono(InformacionAdicionalPLResponseDto.class)
				.block();
	}

	@Override
	public void eliminar(int idInformacion) {
		webClient.delete().uri("/gestalab/informacion-adicional-pl/{id}", idInformacion)
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	@Override
	public List<InformacionAdicionalPLResponseDto> listarPorPlan(int idPlan) {
		return webClient.get().uri("/gestalab/informacion-adicional-pl/plan/{idPlan}", idPlan)
				.retrieve()
				.bodyToFlux(InformacionAdicionalPLResponseDto.class)
				.collectList()
				.block();
	}

}
