package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class DetalleCResponseDto {
	
	private int idDetalleC;
	private int cantidadPuntosDetalleC;
	private double precioUnitarioDetalleC;
	private double precioTotalDetalleC;
	private String condicionDetalleC;
	private CatalogoParametroCResponseDto fkParametro;
	private LmpCResponseDto fkLmp;
	private DescripcionServicioCResponseDto fkDescripcionServicio;
	private PlazoEntregaCResponseDto fkPlazoEntrega;

}
