package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.ParametroAnalizarPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.ParametroAnalizarPLResponseDto;

public interface IParametroAnalizarPLService {

	List<ParametroAnalizarPLResponseDto> listarParametros();
	ParametroAnalizarPLResponseDto guardar(ParametroAnalizarPLRequestDto dto);
	void eliminar(int idParametroPL);
	List<ParametroAnalizarPLResponseDto> listarPorPlan(int idPlan);

}
