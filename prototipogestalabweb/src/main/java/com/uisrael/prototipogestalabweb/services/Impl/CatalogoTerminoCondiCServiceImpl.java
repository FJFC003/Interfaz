package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.CatalogoTerminoCondiCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CatalogoTerminoCondiCResponseDto;
import com.uisrael.prototipogestalabweb.services.ICatalogoTerminoCondiCService;
@Service
public class CatalogoTerminoCondiCServiceImpl implements ICatalogoTerminoCondiCService{
	
	private final WebClient webClient;

	public CatalogoTerminoCondiCServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<CatalogoTerminoCondiCResponseDto> listarTerminos() {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/catalogoterminos")
				.retrieve()
				.bodyToFlux(CatalogoTerminoCondiCResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarTermino(CatalogoTerminoCondiCRequestDto termino) {
		// TODO Auto-generated method stub
		webClient.post().uri("/gestalab/catalogoterminos")
		.bodyValue(termino)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

	@Override
	public CatalogoTerminoCondiCResponseDto buscarPorId(int idTerminoC) {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/catalogoterminos/{id}", idTerminoC)
				.retrieve()
				.bodyToMono(CatalogoTerminoCondiCResponseDto.class)
				.block();
	}

	@Override
	public void eliminarTermino(int idTerminoC) {
		// TODO Auto-generated method stub
		webClient.delete().uri("/gestalab/catalogoterminos/{id}", idTerminoC)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
