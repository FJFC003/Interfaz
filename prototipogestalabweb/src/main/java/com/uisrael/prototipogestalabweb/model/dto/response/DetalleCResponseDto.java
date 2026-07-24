package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class DetalleCResponseDto {
	
	private int idDetalleC;
	private String descripcionDetalleC;
	private String plazoEntregaDetalleC;
	private int cantidadPuntosDetalleC;
	private double precioUnitarioDetalleC;
	private double precioTotalDetalleC;
	private String condicionDetalleC;
	private CatalogoParametroCResponseDto fkParametro;
	private CatalogoNormServiCResponseDto fkNormaServicio;

}
