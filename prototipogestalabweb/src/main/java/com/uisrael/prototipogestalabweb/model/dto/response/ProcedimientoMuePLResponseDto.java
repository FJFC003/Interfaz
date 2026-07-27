package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class ProcedimientoMuePLResponseDto {

	private int idProcedimiento;
	private int noItem;
	private String Tipo;
	private String descripcion;
	private String precausiones;
	private PlanMuestreoPLResponseDto fkPlanMuestreo;

}
