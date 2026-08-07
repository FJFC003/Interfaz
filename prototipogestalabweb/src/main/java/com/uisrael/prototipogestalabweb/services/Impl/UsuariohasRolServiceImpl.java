package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.UsuariohasRolRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.UsuariohasRolResponseDto;
import com.uisrael.prototipogestalabweb.services.IUsuariohasRolService;

@Service
public class UsuariohasRolServiceImpl implements IUsuariohasRolService{
	
	private final WebClient webClient;

	public UsuariohasRolServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public UsuariohasRolResponseDto guardarUsuariohasRol(UsuariohasRolRequestDto usuarioRol) {
		return webClient.post().uri("/gestalab/UsuariohasRol")
				.bodyValue(usuarioRol)
				.retrieve()
				.bodyToMono(UsuariohasRolResponseDto.class)
				.block();
	}

	@Override
	public List<UsuariohasRolResponseDto> listarUsuariohasRol() {
		return webClient.get().uri("/gestalab/UsuariohasRol")
				.retrieve()
				.bodyToFlux(UsuariohasRolResponseDto.class)
				.collectList()
				.block();
	}

}
