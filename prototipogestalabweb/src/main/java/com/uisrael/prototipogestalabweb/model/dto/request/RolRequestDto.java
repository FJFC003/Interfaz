package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class RolRequestDto {
	
	private int idRol;
	private String nombre;
	private String descripcion;
	private boolean estadoRol;

}
