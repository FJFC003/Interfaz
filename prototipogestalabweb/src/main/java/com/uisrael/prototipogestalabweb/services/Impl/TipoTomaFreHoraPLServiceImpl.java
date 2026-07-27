package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.TipoTomaFreHoraPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.TipoTomaFreHoraPLResponseDto;
import com.uisrael.prototipogestalabweb.services.ITipoTomaFreHoraPLService;

@Service
public class TipoTomaFreHoraPLServiceImpl implements ITipoTomaFreHoraPLService {

	private final WebClient webClient;

	public TipoTomaFreHoraPLServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<TipoTomaFreHoraPLResponseDto> listarTiposToma() {
		return webClient.get().uri("/gestalab/tipo-toma-fre-hora-pl")
				.retrieve()
				.bodyToFlux(TipoTomaFreHoraPLResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public TipoTomaFreHoraPLResponseDto guardar(TipoTomaFreHoraPLRequestDto dto) {
		return webClient.post().uri("/gestalab/tipo-toma-fre-hora-pl")
				.bodyValue(dto)
				.retrieve()
				.bodyToMono(TipoTomaFreHoraPLResponseDto.class)
				.block();
	}

	@Override
	public void eliminar(int idTipoFre) {
		webClient.delete().uri("/gestalab/tipo-toma-fre-hora-pl/{id}", idTipoFre)
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	@Override
	public List<TipoTomaFreHoraPLResponseDto> listarPorPlan(int idPlan) {
		return webClient.get().uri("/gestalab/tipo-toma-fre-hora-pl/plan/{idPlan}", idPlan)
				.retrieve()
				.bodyToFlux(TipoTomaFreHoraPLResponseDto.class)
				.collectList()
				.block();
	}

}
