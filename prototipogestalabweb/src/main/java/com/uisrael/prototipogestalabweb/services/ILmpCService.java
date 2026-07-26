package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.LmpCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.LmpCResponseDto;

public interface ILmpCService {
	
	List<LmpCResponseDto> listarLmpCs();
	void guardarLmpC(LmpCRequestDto item);
	LmpCResponseDto buscarPorId(int idLmpC);
	void eliminarLmpC(int idLmpC);

}
