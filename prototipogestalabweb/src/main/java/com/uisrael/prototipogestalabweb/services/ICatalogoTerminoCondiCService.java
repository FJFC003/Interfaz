package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.CatalogoTerminoCondiCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CatalogoTerminoCondiCResponseDto;

public interface ICatalogoTerminoCondiCService {
	
	List<CatalogoTerminoCondiCResponseDto> listarTerminos();
	void guardarTermino(CatalogoTerminoCondiCRequestDto termino);
	CatalogoTerminoCondiCResponseDto buscarPorId(int idTerminoC);
	void eliminarTermino(int idTerminoC);

}
