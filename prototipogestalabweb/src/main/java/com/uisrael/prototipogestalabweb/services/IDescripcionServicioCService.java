package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.DescripcionServicioCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DescripcionServicioCResponseDto;

public interface IDescripcionServicioCService {
	
	List<DescripcionServicioCResponseDto> listarDescripcionServicioCs();
	void guardarDescripcionServicioC(DescripcionServicioCRequestDto item);
	DescripcionServicioCResponseDto buscarPorId(int idDescripcionServicioC);
	void eliminarDescripcionServicioC(int idDescripcionServicioC);

}
