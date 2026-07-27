package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class ParametroAnalizarPLRequestDto {

	private int idParametroPL;
	private int noParametroPL;
	private String Parametros;
	private String unidadMedida;
	private String sitioMedicion;
	private String preservacion;
	private int fkPlanMuestreo;

}
