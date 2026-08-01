package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;
import java.util.Map;

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
		return webClient.get().uri("/gestalab/usuario")
				.retrieve().bodyToFlux(UsuarioResponseDto.class).collectList().block();
	}

	@Override
	public UsuarioResponseDto guardarUsuarios(UsuarioRequestDto usuario) {
		return webClient.post().uri("/gestalab/usuario")
				.bodyValue(usuario).retrieve().bodyToMono(UsuarioResponseDto.class).block();
	}

	@Override
	public void eliminarUsuarios(int idUsuario) {
		webClient.delete().uri("/gestalab/usuario/{id}", idUsuario)
				.retrieve().toBodilessEntity().block();
	}

	@Override
	public UsuarioResponseDto cambiarContrasenia(int idUsuario, String contraseniaEnClaro) {
		return webClient.put().uri("/gestalab/usuario/contrasenia/{id}", idUsuario)
				.bodyValue(Map.of("contrasenia", contraseniaEnClaro))
				.retrieve().bodyToMono(UsuarioResponseDto.class).block();
	}

}
