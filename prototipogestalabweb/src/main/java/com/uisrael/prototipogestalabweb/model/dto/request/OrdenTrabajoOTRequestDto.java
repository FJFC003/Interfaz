package com.uisrael.prototipogestalabweb.model.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class OrdenTrabajoOTRequestDto {

	private int idOT;
	private int noItemOT;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaEmisionOT;

	private int fkResponsableEmision;
	private int fkTecnicoAsignado;
	private String nombrePersonaContactoOT;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaCierreCampoOT;

	private int fkPlanMuestreo;

}
