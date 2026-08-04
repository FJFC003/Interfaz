package com.uisrael.prototipogestalabweb.services.Impl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.RestablecerConTokenRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.SolicitarRecuperacionRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.SolicitudRecuperacionResponseDto;
import com.uisrael.prototipogestalabweb.services.IRecuperacionAccesoService;

@Service
public class RecuperacionAccesoServiceImpl implements IRecuperacionAccesoService {

	private final WebClient webClient;

	public RecuperacionAccesoServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public SolicitudRecuperacionResponseDto solicitar(SolicitarRecuperacionRequestDto peticion) {
		return webClient.post().uri("/gestalab/recuperacion/solicitar")
				.bodyValue(peticion)
				.retrieve()
				.bodyToMono(SolicitudRecuperacionResponseDto.class)
				.block();
	}

	@Override
	public void validarToken(String token) {
		webClient.get().uri("/gestalab/recuperacion/validar/" + token)
				.retrieve()
				.bodyToMono(Void.class)
				.block();
	}

	@Override
	public void restablecer(RestablecerConTokenRequestDto peticion) {
		webClient.post().uri("/gestalab/recuperacion/restablecer")
				.bodyValue(peticion)
				.retrieve()
				.bodyToMono(Void.class)
				.block();
	}

}