package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.RolRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.RolResponseDto;

public interface IRolService {
	
	List<RolResponseDto> listarRoles();
	void guardarRoles(RolRequestDto rol);
	RolResponseDto buscarPorId(int idRol);
	void eliminarRol(int idRol);

}
