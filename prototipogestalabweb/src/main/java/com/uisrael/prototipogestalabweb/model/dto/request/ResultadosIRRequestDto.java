package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class ResultadosIRRequestDto {
	
	private int idResultados;
	private int noItem;
	private String parametros;
	private String metodoReferencial;
	private String unidad;
	private String resultado;
	private String incertidumbre;
	private String LMP;
	private String conformidad;
	private int fkInforme;

}
