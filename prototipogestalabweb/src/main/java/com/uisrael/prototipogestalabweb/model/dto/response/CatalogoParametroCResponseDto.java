package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class CatalogoParametroCResponseDto {
	
	private int idParametroC;
	private CondicionParametroCResponseDto fkCondicionParametro;
	private String ensayoParametroC;
	private String tecnicaParametroC;
	private String procedimientoInternoParametroC;
	private String normaReferencialParametroC;
	private String unidadParametroC;
	private String rangoTrabajoParametroC;
	private String componenteParametroC;

}
