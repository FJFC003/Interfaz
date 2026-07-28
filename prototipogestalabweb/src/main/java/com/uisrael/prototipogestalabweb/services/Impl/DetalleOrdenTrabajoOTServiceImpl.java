package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.DetalleOrdenTrabajoOTRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DetalleOrdenTrabajoOTResponseDto;
import com.uisrael.prototipogestalabweb.services.IDetalleOrdenTrabajoOTService;

@Service
public class DetalleOrdenTrabajoOTServiceImpl implements IDetalleOrdenTrabajoOTService {

	private final WebClient webClient;

	public DetalleOrdenTrabajoOTServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public DetalleOrdenTrabajoOTResponseDto guardar(DetalleOrdenTrabajoOTRequestDto dto) {
		return webClient.post().uri("/gestalab/detalle-orden-trabajo")
				.bodyValue(dto).retrieve().bodyToMono(DetalleOrdenTrabajoOTResponseDto.class).block();
	}

	@Override
	public void eliminar(int idDetalleOrdenOT) {
		webClient.delete().uri("/gestalab/detalle-orden-trabajo/{id}", idDetalleOrdenOT)
				.retrieve().toBodilessEntity().block();
	}

	@Override
	public List<DetalleOrdenTrabajoOTResponseDto> listarPorOrden(int idOT) {
		return webClient.get().uri("/gestalab/detalle-orden-trabajo/orden/{idOT}", idOT)
				.retrieve().bodyToFlux(DetalleOrdenTrabajoOTResponseDto.class).collectList().block();
	}

}
