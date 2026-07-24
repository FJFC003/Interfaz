package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.DetalleCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DetalleCResponseDto;

public interface IDetalleCService {
	
	List<DetalleCResponseDto> listarDetalles();
	void guardarDetalle(DetalleCRequestDto detalle);
	DetalleCResponseDto buscarPorId(int idDetalleC);
	void eliminarDetalle(int idDetalleC);

}
