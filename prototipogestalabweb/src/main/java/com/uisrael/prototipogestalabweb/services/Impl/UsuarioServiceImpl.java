package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.UsuarioRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.UsuarioResponseDto;
import com.uisrael.prototipogestalabweb.services.IUsuarioService;

@Service
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
	public UsuarioResponseDto guardarUsuarios(UsuarioRequestDto usuario) {
		// TODO Auto-generated method stub
		return webClient.post().uri("/gestalab/usuario")
				.bodyValue(usuario)
				.retrieve()
				.bodyToMono(UsuarioResponseDto.class)
				.block();
	}

}
