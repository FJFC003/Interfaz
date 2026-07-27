package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.PlanMuestreoPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.PlanMuestreoPLResponseDto;
import com.uisrael.prototipogestalabweb.services.IPlanMuestreoPLService;

@Service
public class PlanMuestreoPLServiceImpl implements IPlanMuestreoPLService {

	private final WebClient webClient;

	public PlanMuestreoPLServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<PlanMuestreoPLResponseDto> listarPlanes() {
		return webClient.get().uri("/gestalab/plan-muestreo-pl")
				.retrieve()
				.bodyToFlux(PlanMuestreoPLResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public PlanMuestreoPLResponseDto guardar(PlanMuestreoPLRequestDto dto) {
		return webClient.post().uri("/gestalab/plan-muestreo-pl")
				.bodyValue(dto)
				.retrieve()
				.bodyToMono(PlanMuestreoPLResponseDto.class)
				.block();
	}

	@Override
	public PlanMuestreoPLResponseDto buscarPorId(int idPlan) {
		return webClient.get().uri("/gestalab/plan-muestreo-pl/{id}", idPlan)
				.retrieve()
				.bodyToMono(PlanMuestreoPLResponseDto.class)
				.block();
	}

	@Override
	public void eliminar(int idPlan) {
		webClient.delete().uri("/gestalab/plan-muestreo-pl/{id}", idPlan)
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	@Override
	public List<PlanMuestreoPLResponseDto> listarPorCotizacion(int idCotizacionC) {
		return webClient.get().uri("/gestalab/plan-muestreo-pl/cotizacion/{id}", idCotizacionC)
				.retrieve()
				.bodyToFlux(PlanMuestreoPLResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public List<PlanMuestreoPLResponseDto> listarPorDetalle(int idDetalleC) {
		return webClient.get().uri("/gestalab/plan-muestreo-pl/detalle/{id}", idDetalleC)
				.retrieve()
				.bodyToFlux(PlanMuestreoPLResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public List<PlanMuestreoPLResponseDto> listarPorResponsable(int idEmpleado) {
		return webClient.get().uri("/gestalab/plan-muestreo-pl/responsable/{id}", idEmpleado)
				.retrieve()
				.bodyToFlux(PlanMuestreoPLResponseDto.class)
				.collectList()
				.block();
	}

}
