package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class DetalleCRequestDto {
	
	private int idDetalleC;
	private int cantidadPuntosDetalleC;
	private double precioUnitarioDetalleC;
	private double precioTotalDetalleC;
	private String condicionDetalleC;
	private int fkCotizacion;
	private int fkParametro;
	private int fkLmp;
	private int fkDescripcionServicio;
	private int fkPlazoEntrega;

}
