package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.AprobacionCotizacionRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.CotizacionCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CotizacionCResponseDto;

public interface ICotizacionCService {

	List<CotizacionCResponseDto> listarCotizaciones();
	CotizacionCResponseDto guardarCotizacion(CotizacionCRequestDto cotizacion);
	CotizacionCResponseDto buscarPorId(int idCotizacionC);
	void eliminarCotizacion(int idCotizacionC);

	// Flujo de aprobacion (Coordinadora Comercial)
	CotizacionCResponseDto aprobar(int idCotizacionC, AprobacionCotizacionRequestDto aprobacion);
	CotizacionCResponseDto rechazar(int idCotizacionC);


}
