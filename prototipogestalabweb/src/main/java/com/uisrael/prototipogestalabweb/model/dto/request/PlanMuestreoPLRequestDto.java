package com.uisrael.prototipogestalabweb.model.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class PlanMuestreoPLRequestDto {

	private int idPlan;
	private String codigoPlan;
	private String ObjetivoPlan;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaElaboracion;

	private int fkResponsable;
	private int fkDetalleCotizacion;

}
