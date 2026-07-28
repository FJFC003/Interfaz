package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.EEPPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EEPPLResponseDto;
import com.uisrael.prototipogestalabweb.services.IEEPPLService;

@Service
public class EEPPLServiceImpl implements IEEPPLService {

	private final WebClient webClient;

	public EEPPLServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<EEPPLResponseDto> listarEpp() {
		return webClient.get().uri("/gestalab/eeppl")
				.retrieve()
				.bodyToFlux(EEPPLResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public EEPPLResponseDto guardar(EEPPLRequestDto dto) {
		return webClient.post().uri("/gestalab/eeppl")
				.bodyValue(dto)
				.retrieve()
				.bodyToMono(EEPPLResponseDto.class)
				.block();
	}

	@Override
	public void eliminar(int idEEP) {
		webClient.delete().uri("/gestalab/eeppl/{id}", idEEP)
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	@Override
	public EEPPLResponseDto buscarPorId(int idEEP) {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/eeppl/{id}", idEEP)
				.retrieve().bodyToMono(EEPPLResponseDto.class).block();
	}

}
