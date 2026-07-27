package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.InformacionMatrizPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.InformacionMatrizPLResponseDto;

public interface IInformacionMatrizPLService {

	List<InformacionMatrizPLResponseDto> listarMatrices();
	InformacionMatrizPLResponseDto guardar(InformacionMatrizPLRequestDto dto);
	void eliminar(int idInfoMatriz);
	List<InformacionMatrizPLResponseDto> listarPorPlan(int idPlan);

}
