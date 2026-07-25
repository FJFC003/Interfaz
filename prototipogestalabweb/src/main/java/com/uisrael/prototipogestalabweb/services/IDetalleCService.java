package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.DetalleCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DetalleCResponseDto;

public interface IDetalleCService {
	
	List<DetalleCResponseDto> listarDetalles();
	void guardarDetalle(DetalleCRequestDto detalle);
	DetalleCResponseDto buscarPorId(int idDetalleC);
	List<DetalleCResponseDto> listarPorCotizacion(int idCotizacion);
	void eliminarDetalle(int idDetalleC);

}
