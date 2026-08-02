package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class CondicionAmbientalIRResponseDto {
	
	private int idCondi;
	private String noAlicuota;
	private String horaToma;
	private String temperatura;
	private String humedad;
	private String precipitacion;

}
