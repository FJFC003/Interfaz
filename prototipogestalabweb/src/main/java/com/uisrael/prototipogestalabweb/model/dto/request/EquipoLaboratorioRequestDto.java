package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class EquipoLaboratorioRequestDto {
	
	private int idEquipoLab;
	private String nombre;
	private String marca;
	private String modelo;
	private String serie;
	private String codigoInterno;
	private boolean estadoEquipoLab = true;

}
