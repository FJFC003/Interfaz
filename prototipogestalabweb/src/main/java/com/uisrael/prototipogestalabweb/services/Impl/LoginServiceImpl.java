package com.uisrael.prototipogestalabweb.services.Impl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.LoginRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.LoginResponseDto;
import com.uisrael.prototipogestalabweb.services.ILoginService;

@Service
public class LoginServiceImpl implements ILoginService{

	private final WebClient webClient;
	
	public LoginServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public LoginResponseDto login(LoginRequestDto credenciales) {
		// TODO Auto-generated method stub
		return webClient.post().uri("/gestalab/login/login")
				.bodyValue(credenciales)
				.retrieve()
				.bodyToMono(LoginResponseDto.class)
				.block();
	}
	
	

}
