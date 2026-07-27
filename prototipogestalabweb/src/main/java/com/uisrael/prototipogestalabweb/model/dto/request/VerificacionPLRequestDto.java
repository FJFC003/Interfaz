package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class VerificacionPLRequestDto {

	private int idVerificacion;
	private int noItem;
	private boolean tipoMatrizDeclarada;
	private boolean frecuanciaDeclarada;
	private boolean sitiosDeclarados;
	private int fkPlanMuestreo;

}
