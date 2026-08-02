package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.integrador.InformeCompletoIRRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.InformeResultadosIRResponseDto;
import com.uisrael.prototipogestalabweb.services.IInformeResultadosIRService;

@Service
public class InformeResultadosIRServiceImpl implements IInformeResultadosIRService{
	
	private final WebClient webClient;

	public InformeResultadosIRServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<InformeResultadosIRResponseDto> listarInformes() {
		return webClient.get().uri("/gestalab/informe")
				.retrieve().bodyToFlux(InformeResultadosIRResponseDto.class).collectList().block();
	}

	@Override
	public InformeResultadosIRResponseDto buscarPorId(int idInforme) {
		return webClient.get().uri("/gestalab/informe/{id}", idInforme)
				.retrieve().bodyToMono(InformeResultadosIRResponseDto.class).block();
	}

	@Override
	public InformeResultadosIRResponseDto buscarPorOrden(int idOT) {
		return webClient.get().uri("/gestalab/informe/orden/{id}", idOT)
				.retrieve().bodyToMono(InformeResultadosIRResponseDto.class).block();
	}

	@Override
	public void eliminar(int idInforme) {
		webClient.delete().uri("/gestalab/informe/{id}", idInforme)
				.retrieve().toBodilessEntity().block();
	}

	@Override
	public InformeResultadosIRResponseDto guardarCompleto(InformeCompletoIRRequestDto dto) {
		return webClient.post().uri("/gestalab/informe/completo")
				.bodyValue(dto).retrieve()
				.bodyToMono(InformeResultadosIRResponseDto.class).block();
	}

}
