package com.uisrael.prototipogestalabweb.model.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class DetalleOrdenTrabajoOTRequestDto {
	
	private int idDetalleOrdenOT;
	private int noItemDetalleOrdenOT;
	private String descripcionActividadDetalleOrdenOT;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaPlanificadaDetalleOrdenOT;

	private int puntosPlanificadosDetalleOrdenOT;
	private int puntosEjecutadosDetalleOrdenOT;
	private int fkOrdenTrabajo;

}
