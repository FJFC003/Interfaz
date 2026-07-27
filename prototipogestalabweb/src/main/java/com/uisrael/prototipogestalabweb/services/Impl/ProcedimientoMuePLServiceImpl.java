package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.ProcedimientoMuePLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.ProcedimientoMuePLResponseDto;
import com.uisrael.prototipogestalabweb.services.IProcedimientoMuePLService;

@Service
public class ProcedimientoMuePLServiceImpl implements IProcedimientoMuePLService {

	private final WebClient webClient;

	public ProcedimientoMuePLServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<ProcedimientoMuePLResponseDto> listarProcedimientos() {
		return webClient.get().uri("/gestalab/procedimiento-mue-pl")
				.retrieve()
				.bodyToFlux(ProcedimientoMuePLResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public ProcedimientoMuePLResponseDto guardar(ProcedimientoMuePLRequestDto dto) {
		return webClient.post().uri("/gestalab/procedimiento-mue-pl")
				.bodyValue(dto)
				.retrieve()
				.bodyToMono(ProcedimientoMuePLResponseDto.class)
				.block();
	}

	@Override
	public void eliminar(int idProcedimiento) {
		webClient.delete().uri("/gestalab/procedimiento-mue-pl/{id}", idProcedimiento)
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	@Override
	public List<ProcedimientoMuePLResponseDto> listarPorPlan(int idPlan) {
		return webClient.get().uri("/gestalab/procedimiento-mue-pl/plan/{idPlan}", idPlan)
				.retrieve()
				.bodyToFlux(ProcedimientoMuePLResponseDto.class)
				.collectList()
				.block();
	}

}
