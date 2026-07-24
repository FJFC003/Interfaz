package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.DetalleCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DetalleCResponseDto;
import com.uisrael.prototipogestalabweb.services.IDetalleCService;
@Service
public class DetalleCServiceImpl implements IDetalleCService{
	
	private final WebClient webClient;

	public DetalleCServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<DetalleCResponseDto> listarDetalles() {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/detallecotizacion")
				.retrieve()
				.bodyToFlux(DetalleCResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarDetalle(DetalleCRequestDto detalle) {
		// TODO Auto-generated method stub
		webClient.post().uri("/gestalab/detallecotizacion")
		.bodyValue(detalle)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

	@Override
	public DetalleCResponseDto buscarPorId(int idDetalleC) {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/detallecotizacion/{id}", idDetalleC)
		.retrieve()
		.bodyToMono(DetalleCResponseDto.class)
		.block();
	}

	@Override
	public void eliminarDetalle(int idDetalleC) {
		// TODO Auto-generated method stub
		webClient.delete().uri("/gestalab/detallecotizacion/{id}", idDetalleC)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
