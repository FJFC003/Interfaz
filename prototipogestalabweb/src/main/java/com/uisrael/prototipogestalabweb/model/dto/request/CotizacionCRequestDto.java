package com.uisrael.prototipogestalabweb.model.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CotizacionCRequestDto {
	
	private int idCotizacionC;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
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
	private int fkCliente;
	private int fkEmpleado;
	private int fkNormaServicio;

}
