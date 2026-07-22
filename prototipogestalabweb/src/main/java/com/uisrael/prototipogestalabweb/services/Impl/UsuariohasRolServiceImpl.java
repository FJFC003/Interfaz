package com.uisrael.prototipogestalabweb.services.Impl;

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
		// TODO Auto-generated method stub
		return webClient.post().uri("/gestalab/UsuariohasRol")
				.bodyValue(usuarioRol)
				.retrieve()
				.bodyToMono(UsuariohasRolResponseDto.class)
				.block();
	}

}
