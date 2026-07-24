package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.CatalogoNormServiCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CatalogoNormServiCResponseDto;

public interface ICatalogoNormServiCService {
	
	List<CatalogoNormServiCResponseDto> listarNormas();
	void guardarNorma(CatalogoNormServiCRequestDto norma);
	CatalogoNormServiCResponseDto buscarPorId(int idCatalogoNormServi);
	void eliminarNorma(int idCatalogoNormServi);

}
