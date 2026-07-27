package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class ProcedimientoMuePLRequestDto {

	private int idProcedimiento;
	private int noItem;
	private String Tipo;
	private String descripcion;
	private String precausiones;
	private int fkPlanMuestreo;

}
