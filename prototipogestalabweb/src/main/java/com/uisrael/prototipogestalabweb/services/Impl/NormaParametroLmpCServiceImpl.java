package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.NormaParametroLmpCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.NormaParametroLmpCResponseDto;
import com.uisrael.prototipogestalabweb.services.INormaParametroLmpCService;

@Service
public class NormaParametroLmpCServiceImpl implements INormaParametroLmpCService{
	
	private final WebClient webClient;

	public NormaParametroLmpCServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}
	
	@Override
	public List<NormaParametroLmpCResponseDto> listarPorNorma(int idCatalogoNormServi) {
		return webClient.get().uri("/gestalab/normaparametrolmp/pornorma/{idNorma}", idCatalogoNormServi)
				.retrieve()
				.bodyToFlux(NormaParametroLmpCResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarAsociacion(NormaParametroLmpCRequestDto asociacion) {
		webClient.post().uri("/gestalab/normaparametrolmp")
		.bodyValue(asociacion)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

	@Override
	public void eliminarAsociacion(int idNormaParametroLmpC) {
		webClient.delete().uri("/gestalab/normaparametrolmp/{id}", idNormaParametroLmpC)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
