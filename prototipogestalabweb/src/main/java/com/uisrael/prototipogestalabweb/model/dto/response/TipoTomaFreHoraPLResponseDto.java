package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class TipoTomaFreHoraPLResponseDto {

	private int idTipoFre;
	private int noItem;
	private String frecuencia;
	private String tipo;
	private String alicuotas;
	private PlanMuestreoPLResponseDto fkPlanMuestreo;

}
