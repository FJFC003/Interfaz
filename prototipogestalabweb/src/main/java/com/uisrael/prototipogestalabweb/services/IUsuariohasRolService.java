package com.uisrael.prototipogestalabweb.services;


import com.uisrael.prototipogestalabweb.model.dto.request.UsuariohasRolRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.UsuariohasRolResponseDto;


public interface IUsuariohasRolService {
	
	UsuariohasRolResponseDto guardarUsuariohasRol(UsuariohasRolRequestDto usuarioRol);

}
