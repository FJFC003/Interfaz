package com.uisrael.prototipogestalabweb.model.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class CotizacionCResponseDto {
	
	private int idCotizacionC;
	private Date fechaElaboracionCotizacionC;
	private String vigenciaDiasCotizacionC;
	private String elaboradoPorCotizacionC;
	private double subtotalAgua;
	private double subtotalRuido;
	private double subtotalEmiciones;
	private double subtotalCalidad;
	private double subtotalSuelo;
	private double costoLogistica;
	private double Iva;
	private double TotalCotizacionC;
	private boolean estadoCotizacionC;
	private ClienteCResponseDto fkCliente;
	private EmpleadoResponseDto fkEmpleado;

}
