package com.uisrael.prototipogestalabweb.services;

import com.uisrael.prototipogestalabweb.model.dto.request.RestablecerConTokenRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.SolicitarRecuperacionRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.SolicitudRecuperacionResponseDto;

public interface IRecuperacionAccesoService {
	
	SolicitudRecuperacionResponseDto solicitar(SolicitarRecuperacionRequestDto peticion);

	void validarToken(String token);

	void restablecer(RestablecerConTokenRequestDto peticion);


}
