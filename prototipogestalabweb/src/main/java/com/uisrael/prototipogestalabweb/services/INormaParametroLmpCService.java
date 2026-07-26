package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.NormaParametroLmpCRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.NormaParametroLmpCResponseDto;

public interface INormaParametroLmpCService {
	
	List<NormaParametroLmpCResponseDto> listarPorNorma(int idCatalogoNormServi);
	void guardarAsociacion(NormaParametroLmpCRequestDto asociacion);
	void eliminarAsociacion(int idNormaParametroLmpC);

}
