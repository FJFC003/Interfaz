package com.uisrael.prototipogestalabweb.model.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class InformeResultadosIRRequestDto {
	
	private int idInforme;
	private String codigoInforme;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaEmisionInforme;

	private String notasAdvertencia;
	private String nombreResponsable;
	private String conformidadGeneral;
	private int fkDatosLaboratorio;
	private int fkOrdenTrabajo;

}
