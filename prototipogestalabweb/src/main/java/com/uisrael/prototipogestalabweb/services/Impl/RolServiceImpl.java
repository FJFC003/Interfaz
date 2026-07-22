package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.response.RolResponseDto;
import com.uisrael.prototipogestalabweb.services.IRolService;

@Service
public class RolServiceImpl implements IRolService{
	
	private final WebClient webClient;

	public RolServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<RolResponseDto> listarRoles() {
		// TODO Auto-generated method stub
		return webClient.get().uri("/gestalab/rol")
				.retrieve()
				.bodyToFlux(RolResponseDto.class)
				.collectList()
				.block();
	}

}
