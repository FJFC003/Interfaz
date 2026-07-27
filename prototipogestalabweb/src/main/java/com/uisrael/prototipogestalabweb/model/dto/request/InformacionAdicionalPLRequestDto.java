package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class InformacionAdicionalPLRequestDto {

	private int idInformacion;
	private String preguntas;
	private String respuesta;
	private int fkPlanMuestreo;

}
