package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.OrdenTrabajoOTRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.OrdenTrabajoOTResponseDto;
import com.uisrael.prototipogestalabweb.services.IOrdenTrabajoOTService;

@Service
public class OrdenTrabajoOTServiceImpl implements IOrdenTrabajoOTService {

	private final WebClient webClient;

	public OrdenTrabajoOTServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<OrdenTrabajoOTResponseDto> listarOrdenes() {
		return webClient.get().uri("/gestalab/ordentrabajo")
				.retrieve().bodyToFlux(OrdenTrabajoOTResponseDto.class).collectList().block();
	}

	@Override
	public OrdenTrabajoOTResponseDto guardar(OrdenTrabajoOTRequestDto dto) {
		return webClient.post().uri("/gestalab/ordentrabajo")
				.bodyValue(dto).retrieve().bodyToMono(OrdenTrabajoOTResponseDto.class).block();
	}

	@Override
	public OrdenTrabajoOTResponseDto buscarPorId(int idOT) {
		return webClient.get().uri("/gestalab/ordentrabajo/{id}", idOT)
				.retrieve().bodyToMono(OrdenTrabajoOTResponseDto.class).block();
	}

	@Override
	public void eliminar(int idOT) {
		webClient.delete().uri("/gestalab/ordentrabajo/{id}", idOT)
				.retrieve().toBodilessEntity().block();
	}

	@Override
	public List<OrdenTrabajoOTResponseDto> listarPorTecnico(int idEmpleado) {
		return webClient.get().uri("/gestalab/ordentrabajo/tecnico/{id}", idEmpleado)
				.retrieve().bodyToFlux(OrdenTrabajoOTResponseDto.class).collectList().block();
	}

	@Override
	public List<OrdenTrabajoOTResponseDto> listarPorPlan(int idPlan) {
		return webClient.get().uri("/gestalab/ordentrabajo/plan/{id}", idPlan)
				.retrieve().bodyToFlux(OrdenTrabajoOTResponseDto.class).collectList().block();
	}

}
