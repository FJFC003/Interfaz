package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.RecursosCronoPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.RecursosCronoPLResponseDto;
import com.uisrael.prototipogestalabweb.services.IRecursosCronoPLService;

@Service
public class RecursosCronoPLServiceImpl implements IRecursosCronoPLService {

	private final WebClient webClient;

	public RecursosCronoPLServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<RecursosCronoPLResponseDto> listarRecursos() {
		return webClient.get().uri("/gestalab/recursos-crono-pl")
				.retrieve()
				.bodyToFlux(RecursosCronoPLResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public RecursosCronoPLResponseDto guardar(RecursosCronoPLRequestDto dto) {
		return webClient.post().uri("/gestalab/recursos-crono-pl")
				.bodyValue(dto)
				.retrieve()
				.bodyToMono(RecursosCronoPLResponseDto.class)
				.block();
	}

	@Override
	public void eliminar(int idRecursos) {
		webClient.delete().uri("/gestalab/recursos-crono-pl/{id}", idRecursos)
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	@Override
	public List<RecursosCronoPLResponseDto> listarPorPlan(int idPlan) {
		return webClient.get().uri("/gestalab/recursos-crono-pl/plan/{idPlan}", idPlan)
				.retrieve()
				.bodyToFlux(RecursosCronoPLResponseDto.class)
				.collectList()
				.block();
	}

}
