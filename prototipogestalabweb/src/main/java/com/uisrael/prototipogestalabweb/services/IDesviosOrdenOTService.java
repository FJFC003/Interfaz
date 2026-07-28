package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.DesviosOrdenOTRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DesviosOrdenOTResponseDto;

public interface IDesviosOrdenOTService {
	
	DesviosOrdenOTResponseDto guardar(DesviosOrdenOTRequestDto dto);
	void eliminar(int idDesviosOrdenOT);
	List<DesviosOrdenOTResponseDto> listarPorOrden(int idOT);

}
