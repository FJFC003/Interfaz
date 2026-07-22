package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.UsuarioRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.UsuarioResponseDto;

public interface IUsuarioService {
	
	List<UsuarioResponseDto> listarUsuarios();
	UsuarioResponseDto guardarUsuarios(UsuarioRequestDto usuario);

}
