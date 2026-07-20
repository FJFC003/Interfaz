package com.uisrael.prototipogestalabweb.model.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class EmpleadoResponseDto {
	
	private int idEmpleado;
	private String nombre;
	private String apellido;
	private String ci;
	private String correo;
	private String direccion;
	private Date fechaIngreso;
	private Date fechaSalida;
	private boolean estadoEmpleado;
	

}
