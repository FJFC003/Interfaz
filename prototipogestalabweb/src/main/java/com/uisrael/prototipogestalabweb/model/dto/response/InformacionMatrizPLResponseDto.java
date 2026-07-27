package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class InformacionMatrizPLResponseDto {

	private int idInfoMatriz;
	private int noItem;
	private String tipoMatriz;
	private String Ubicacion;
	private String descripcionDelPunto;
	private String Accesibilidad;
	private PlanMuestreoPLResponseDto fkPlanMuestreo;

}
