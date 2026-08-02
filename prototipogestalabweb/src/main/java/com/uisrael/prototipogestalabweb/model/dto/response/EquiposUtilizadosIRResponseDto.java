package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class EquiposUtilizadosIRResponseDto {
	
	private int idEquipos;
	private String nombre;
	private String marca;
	private String modelo;
	private String serie;
	private String codigoInterno;

}
