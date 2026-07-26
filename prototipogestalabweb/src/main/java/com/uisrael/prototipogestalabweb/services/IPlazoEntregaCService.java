package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.PlazoEntregaCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.PlazoEntregaCResponseDto;

public interface IPlazoEntregaCService {
	
	List<PlazoEntregaCResponseDto> listarPlazoEntregaCs();
	void guardarPlazoEntregaC(PlazoEntregaCRequestDto item);
	PlazoEntregaCResponseDto buscarPorId(int idPlazoEntregaC);
	void eliminarPlazoEntregaC(int idPlazoEntregaC);

}
