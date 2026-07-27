package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.VerificacionPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.VerificacionPLResponseDto;

public interface IVerificacionPLService {

	List<VerificacionPLResponseDto> listarVerificaciones();
	VerificacionPLResponseDto guardar(VerificacionPLRequestDto dto);
	void eliminar(int idVerificacion);
	List<VerificacionPLResponseDto> listarPorPlan(int idPlan);

}
