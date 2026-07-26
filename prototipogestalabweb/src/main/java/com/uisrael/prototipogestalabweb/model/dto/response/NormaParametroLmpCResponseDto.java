package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class NormaParametroLmpCResponseDto {
	
	private int idNormaParametroLmpC;
	private CatalogoNormServiCResponseDto fkNorma;
	private CatalogoParametroCResponseDto fkParametro;
	private LmpCResponseDto fkLmp;

}
