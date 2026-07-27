package com.uisrael.prototipogestalabweb.model.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class OrdenTrabajoOTResponseDto {

	private int idOT;
	private int noItemOT;
	private Date fechaEmisionOT;
	private EmpleadoResponseDto fkResponsableEmision;
	private EmpleadoResponseDto fkTecnicoAsignado;
	private String nombrePersonaContactoOT;
	private Date fechaCierreCampoOT;
	private PlanMuestreoPLResponseDto fkPlanMuestreo;

}
