package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.UsuarioRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.UsuarioResponseDto;
import com.uisrael.prototipogestalabweb.services.IUsuarioService;

public class UsuarioServiceImpl implements IUsuarioService {
	
	private final WebClient webClient;

	public UsuarioServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<UsuarioResponseDto> listarUsuarios() {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/usuario")
				.retrieve()
				.bodyToFlux(UsuarioResponseDto.class)
				.collectList()
				.block();
	}

	@Override
	public void guardarUsuarios(UsuarioRequestDto usuario) {
		// TODO Auto-generated method stub
		webClient.post().uri("/gestalab/usuario")
		.bodyValue(usuario)
		.retrieve()
		.toBodilessEntity()
		.block();
	}

}
