package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class TipoTomaFreHoraPLRequestDto {

	private int idTipoFre;
	private int noItem;
	private String frecuencia;
	private String tipo;
	private String alicuotas;
	private int fkPlanMuestreo;

}
