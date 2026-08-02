package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class EquipoLaboratorioResponseDto {
	
	private int idEquipoLab;
	private String nombre;
	private String marca;
	private String modelo;
	private String serie;
	private String codigoInterno;
	private boolean estadoEquipoLab;

}
