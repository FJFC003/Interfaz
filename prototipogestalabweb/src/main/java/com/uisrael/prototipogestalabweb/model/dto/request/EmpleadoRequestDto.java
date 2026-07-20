package com.uisrael.prototipogestalabweb.model.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class EmpleadoRequestDto {
		
		private int idEmpleado;
		private String nombre;
		private String apellido;
		private String ci;
		private String correo;
		private String direccion;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date fechaIngreso;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date fechaSalida;
		private boolean estadoEmpleado;

	}
