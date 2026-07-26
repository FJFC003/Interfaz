package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class NormaParametroLmpCRequestDto {
	
	private int idNormaParametroLmpC;
	private int fkNorma;
	private int fkParametro;
	private int fkLmp;

}
