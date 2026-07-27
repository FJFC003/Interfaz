package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.ProcedimientoMuePLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.ProcedimientoMuePLResponseDto;

public interface IProcedimientoMuePLService {

	List<ProcedimientoMuePLResponseDto> listarProcedimientos();
	ProcedimientoMuePLResponseDto guardar(ProcedimientoMuePLRequestDto dto);
	void eliminar(int idProcedimiento);
	List<ProcedimientoMuePLResponseDto> listarPorPlan(int idPlan);

}
