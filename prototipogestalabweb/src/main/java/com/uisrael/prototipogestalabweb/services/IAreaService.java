package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.AreaRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.AreaResponseDto;

public interface IAreaService {
	
	List<AreaResponseDto> listarAreas();
	void guardarAreas(AreaRequestDto area);
	AreaResponseDto buscarPorId(int idArea);
	void eliminarArea(int idArea);

}
