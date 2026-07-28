package com.uisrael.prototipogestalabweb.model.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class PlanMuestreoPLResponseDto {

	private int idPlan;
	private String codigoPlan;
	private String ObjetivoPlan;
	private Date fechaElaboracion;
	private EmpleadoResponseDto fkResponsable;
	private DetalleCResponseDto fkDetalleCotizacion;
	private EEPPLResponseDto fkeep;

	private String estadoPlan;
	private Date fechaEnvioTecnico;

}
