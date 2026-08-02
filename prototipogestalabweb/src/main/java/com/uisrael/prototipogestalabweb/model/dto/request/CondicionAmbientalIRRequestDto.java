package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class CondicionAmbientalIRRequestDto {
	
	private int idCondi;
	private String noAlicuota;
	private String horaToma;
	private String temperatura;
	private String humedad;
	private String precipitacion;
	private int fkInforme;

}
