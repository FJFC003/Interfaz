package com.uisrael.prototipogestalabweb.model.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class InformeResultadosIRResponseDto {
	
	private int idInforme;
	private String codigoInforme;
	private Date fechaEmisionInforme;
	private String notasAdvertencia;
	private String nombreResponsable;
	private String conformidadGeneral;
	private DatosLaboratorioIRResponseDto fkDatosLaboratorio;
	private OrdenTrabajoOTResponseDto fkOrdenTrabajo;
	private String estadoInforme;
	private Date fechaEnvioCoordinacion;

	// ---- Informacion del sitio de muestreo ----
	private String identificacionSitioMuestreo;
	private String coordenadaUtmX;
	private String coordenadaUtmY;
	private String codigoCadenaCustodia;
	private String codigoLaboratorio;
	private String procedimientoTomaMuestra;
	private Date fechaIngresoLaboratorio;

}
