package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class InformacionAdicionalPLResponseDto {
	
	private int idInformacion;
	private String preguntas;
	private String respuesta;
	private PlanMuestreoPLResponseDto fkPlanMuestreo;


}
