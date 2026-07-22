package com.uisrael.prototipogestalabweb.model.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class UsuarioRequestDto {
	
	private int idUsuario;
	private String nombre;
	private String correo;
	private String contrasenia;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaCreacion;
	private boolean estadoUsuario;

}
