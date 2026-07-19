package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class CargoRequestDto {
	
	private int idCargo;
	private String nombre;
	private String descripcion;
	private boolean estadoCargo;

}
