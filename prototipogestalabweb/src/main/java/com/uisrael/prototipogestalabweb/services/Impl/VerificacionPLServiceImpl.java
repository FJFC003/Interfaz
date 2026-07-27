package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.VerificacionPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.VerificacionPLResponseDto;
import com.uisrael.prototipogestalabweb.services.IVerificacionPLService;

@Service
public class VerificacionPLServiceImpl implements IVerificacionPLService {

	private final WebClient webClient;

	public VerificacionPLServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<VerificacionPLResponseDto> listarVerificaciones() {
		return webClient.get().uri("/gestalab/verificacion-pl")
				.retrieve()
				.bodyToFlux(VerificacionPLResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public VerificacionPLResponseDto guardar(VerificacionPLRequestDto dto) {
		return webClient.post().uri("/gestalab/verificacion-pl")
				.bodyValue(dto)
				.retrieve()
				.bodyToMono(VerificacionPLResponseDto.class)
				.block();
	}

	@Override
	public void eliminar(int idVerificacion) {
		webClient.delete().uri("/gestalab/verificacion-pl/{id}", idVerificacion)
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	@Override
	public List<VerificacionPLResponseDto> listarPorPlan(int idPlan) {
		return webClient.get().uri("/gestalab/verificacion-pl/plan/{idPlan}", idPlan)
				.retrieve()
				.bodyToFlux(VerificacionPLResponseDto.class)
				.collectList()
				.block();
	}

}
