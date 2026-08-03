package com.uisrael.prototipogestalabweb.services;

import com.uisrael.prototipogestalabweb.model.dto.request.ConfigurarPreguntaRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.PreguntaSeguridadRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.RestablecerAccesoRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.PreguntaSeguridadResponseDto;

public interface IRecuperacionAccesoService {
	
	PreguntaSeguridadResponseDto obtenerPregunta(PreguntaSeguridadRequestDto peticion);

	void restablecer(RestablecerAccesoRequestDto peticion);

	void configurar(int idUsuario, ConfigurarPreguntaRequestDto peticion);

	boolean tieneConfigurada(int idUsuario);

}
