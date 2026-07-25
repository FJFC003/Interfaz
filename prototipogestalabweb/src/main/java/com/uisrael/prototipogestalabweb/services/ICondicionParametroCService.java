package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.CondicionParametroCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CondicionParametroCResponseDto;

public interface ICondicionParametroCService {
	
	List<CondicionParametroCResponseDto> listarCondiciones();
	void guardarCondicion(CondicionParametroCRequestDto condicion);
	CondicionParametroCResponseDto buscarPorId(int idCondicionParametroC);
	void eliminarCondicion(int idCondicionParametroC);

}
