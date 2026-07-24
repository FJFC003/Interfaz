package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.CatalogoParametroCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CatalogoParametroCResponseDto;

public interface ICatalogoParametroCService {
	
	List<CatalogoParametroCResponseDto> listarParametros();
	void guardarParametro(CatalogoParametroCRequestDto parametro);
	CatalogoParametroCResponseDto buscarPorId(int idParametroC);
	void eliminarParametro(int idParametroC);

}
