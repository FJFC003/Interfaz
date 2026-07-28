package com.uisrael.prototipogestalabweb.model.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class DetalleOrdenTrabajoOTResponseDto {
	
	private int idDetalleOrdenOT;
	private int noItemDetalleOrdenOT;
	private String descripcionActividadDetalleOrdenOT;
	private Date fechaPlanificadaDetalleOrdenOT;
	private int puntosPlanificadosDetalleOrdenOT;
	private int puntosEjecutadosDetalleOrdenOT;

}
