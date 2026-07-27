package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class VerificacionPLResponseDto {

	private int idVerificacion;
	private int noItem;
	private boolean tipoMatrizDeclarada;
	private boolean frecuanciaDeclarada;
	private boolean sitiosDeclarados;
	private PlanMuestreoPLResponseDto fkPlanMuestreo;

}
