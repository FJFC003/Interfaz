package com.uisrael.prototipogestalabweb.services.Impl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.ConfigurarPreguntaRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.PreguntaSeguridadRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.RestablecerAccesoRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.PreguntaSeguridadResponseDto;
import com.uisrael.prototipogestalabweb.services.IRecuperacionAccesoService;

@Service
public class RecuperacionAccesoServiceImpl implements IRecuperacionAccesoService {

	private final WebClient webClient;

	public RecuperacionAccesoServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public PreguntaSeguridadResponseDto obtenerPregunta(PreguntaSeguridadRequestDto peticion) {
		return webClient.post().uri("/gestalab/recuperacion/pregunta")
				.bodyValue(peticion)
				.retrieve()
				.bodyToMono(PreguntaSeguridadResponseDto.class)
				.block();
	}

	@Override
	public void restablecer(RestablecerAccesoRequestDto peticion) {
		webClient.post().uri("/gestalab/recuperacion/restablecer")
				.bodyValue(peticion)
				.retrieve()
				.bodyToMono(Void.class)
				.block();
	}

	@Override
	public void configurar(int idUsuario, ConfigurarPreguntaRequestDto peticion) {
		webClient.put().uri("/gestalab/recuperacion/configurar/" + idUsuario)
				.bodyValue(peticion)
				.retrieve()
				.bodyToMono(Void.class)
				.block();
	}

	@Override
	public boolean tieneConfigurada(int idUsuario) {
		Boolean resultado = webClient.get().uri("/gestalab/recuperacion/configurada/" + idUsuario)
				.retrieve()
				.bodyToMono(Boolean.class)
				.block();
		return Boolean.TRUE.equals(resultado);
	}

}