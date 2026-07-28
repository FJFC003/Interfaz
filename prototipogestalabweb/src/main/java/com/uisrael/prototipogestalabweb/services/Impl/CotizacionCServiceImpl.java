package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.AprobacionCotizacionRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.CotizacionCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CotizacionCResponseDto;
import com.uisrael.prototipogestalabweb.services.ICotizacionCService;

@Service
public class CotizacionCServiceImpl implements ICotizacionCService{
	
	private final WebClient webClient;

	public CotizacionCServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<CotizacionCResponseDto> listarCotizaciones() {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/cotizacion")
				.retrieve()
				.bodyToFlux(CotizacionCResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public CotizacionCResponseDto guardarCotizacion(CotizacionCRequestDto cotizacion) {
		// TODO Auto-generated method stub
		return webClient.post().uri("/gestalab/cotizacion")
				.bodyValue(cotizacion)
				.retrieve()
				.bodyToMono(CotizacionCResponseDto.class)
				.block();
	}

	@Override
	public CotizacionCResponseDto buscarPorId(int idCotizacionC) {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/cotizacion/{id}", idCotizacionC)
				.retrieve()
				.bodyToMono(CotizacionCResponseDto.class)
				.block();
	}

	@Override
	public void eliminarCotizacion(int idCotizacionC) {
		// TODO Auto-generated method stub
		webClient.delete().uri("/gestalab/cotizacion/{id}", idCotizacionC)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

	@Override
	public CotizacionCResponseDto aprobar(int idCotizacionC, AprobacionCotizacionRequestDto aprobacion) {
		// TODO Auto-generated method stub
		return webClient.put().uri("/gestalab/cotizacion/aprobar/{id}", idCotizacionC)
				.bodyValue(aprobacion)
				.retrieve()
				.bodyToMono(CotizacionCResponseDto.class)
				.block();
	}

	@Override
	public CotizacionCResponseDto rechazar(int idCotizacionC) {
		// TODO Auto-generated method stub
		return webClient.put().uri("/gestalab/cotizacion/rechazar/{id}", idCotizacionC)
				.retrieve()
				.bodyToMono(CotizacionCResponseDto.class)
				.block();
	}

}
