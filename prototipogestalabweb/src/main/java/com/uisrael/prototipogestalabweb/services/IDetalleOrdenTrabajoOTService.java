package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.DetalleOrdenTrabajoOTRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DetalleOrdenTrabajoOTResponseDto;

public interface IDetalleOrdenTrabajoOTService {

	DetalleOrdenTrabajoOTResponseDto guardar(DetalleOrdenTrabajoOTRequestDto dto);
	void eliminar(int idDetalleOrdenOT);
	List<DetalleOrdenTrabajoOTResponseDto> listarPorOrden(int idOT);
	
}
