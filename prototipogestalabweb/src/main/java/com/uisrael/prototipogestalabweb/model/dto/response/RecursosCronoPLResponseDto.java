package com.uisrael.prototipogestalabweb.model.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class RecursosCronoPLResponseDto {

	private int idRecursos;
	private Date fechaMuestreo;
	private EmpleadoResponseDto fkTecnico;
	private String recurso;
	private Date horaDefinida;
	private PlanMuestreoPLResponseDto fkPlanMuestreo;

}
