package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class ParametroAnalizarPLResponseDto {

	private int idParametroPL;
	private int noParametroPL;
	private String Parametros;
	private String unidadMedida;
	private String sitioMedicion;
	private String preservacion;
	private PlanMuestreoPLResponseDto fkPlanMuestreo;

}
