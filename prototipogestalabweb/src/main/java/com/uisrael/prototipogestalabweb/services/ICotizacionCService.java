package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.CotizacionCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CotizacionCResponseDto;

public interface ICotizacionCService {
	List<CotizacionCResponseDto> listarCotizaciones();
	CotizacionCResponseDto guardarCotizacion(CotizacionCRequestDto cotizacion);
	CotizacionCResponseDto buscarPorId(int idCotizacionC);
	void eliminarCotizacion(int idCotizacionC);

}
