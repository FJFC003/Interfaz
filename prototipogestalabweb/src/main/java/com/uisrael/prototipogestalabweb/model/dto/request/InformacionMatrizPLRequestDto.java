package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class InformacionMatrizPLRequestDto {

	private int idInfoMatriz;
	private int noItem;
	private String tipoMatriz;
	private String Ubicacion;
	private String descripcionDelPunto;
	private String Accesibilidad;
	private int fkPlanMuestreo;

}
