package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.EEPPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EEPPLResponseDto;

public interface IEEPPLService {

	List<EEPPLResponseDto> listarEpp();
	EEPPLResponseDto guardar(EEPPLRequestDto dto);
	EEPPLResponseDto buscarPorId(int idEEP);
	void eliminar(int idEEP);

}
