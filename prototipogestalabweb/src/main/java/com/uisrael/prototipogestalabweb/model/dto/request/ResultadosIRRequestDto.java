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
	// Se llama 'lmp' en minusculas a proposito: con el nombre 'LMP' el enlace
	// de Spring (th:field) tiene que resolver un getter getLMP(), un caso borde
	// de la especificacion JavaBeans que da problemas al renderizar el formulario.
	private String lmp;
	private String conformidad;
	private int fkInforme;

}
