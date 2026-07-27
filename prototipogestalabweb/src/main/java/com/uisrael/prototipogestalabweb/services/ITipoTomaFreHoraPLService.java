package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.TipoTomaFreHoraPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.TipoTomaFreHoraPLResponseDto;

public interface ITipoTomaFreHoraPLService {

	List<TipoTomaFreHoraPLResponseDto> listarTiposToma();
	TipoTomaFreHoraPLResponseDto guardar(TipoTomaFreHoraPLRequestDto dto);
	void eliminar(int idTipoFre);
	List<TipoTomaFreHoraPLResponseDto> listarPorPlan(int idPlan);

}
