package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class CatalogoParametroCRequestDto {
	
	private int idParametroC;
	private int fkCondicionParametro;
	private String ensayoParametroC;
	private String tecnicaParametroC;
	private String procedimientoInternoParametroC;
	private String normaReferencialParametroC;
	private String unidadParametroC;
	private String rangoTrabajoParametroC;

}
