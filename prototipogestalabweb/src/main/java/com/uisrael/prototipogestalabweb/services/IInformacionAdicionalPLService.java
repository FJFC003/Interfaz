package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.InformacionAdicionalPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.InformacionAdicionalPLResponseDto;

public interface IInformacionAdicionalPLService {

	List<InformacionAdicionalPLResponseDto> listarInformacionAdicional();
	InformacionAdicionalPLResponseDto guardar(InformacionAdicionalPLRequestDto dto);
	void eliminar(int idInformacion);
	List<InformacionAdicionalPLResponseDto> listarPorPlan(int idPlan);

}
