package com.uisrael.prototipogestalabweb.model.dto.response;

import java.util.Date;

import lombok.Data;


@Data
public class UsuarioResponseDto {
	
	private int idUsuario;
	private String nombre;
	private String correo;
	private String contrasenia;
	private Date fechaCreacion;
	private boolean estadoUsuario;

}
