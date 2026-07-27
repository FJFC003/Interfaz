package com.uisrael.prototipogestalabweb.model.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class RecursosCronoPLRequestDto {

	private int idRecursos;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaMuestreo;

	private int fkTecnico;
	private String recurso;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date horaDefinida;

	private int fkPlanMuestreo;

}
