package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class LoginResponseDto {
	
	private int idUsuario;
	private String correo;
	private String contrasenia;
	private String rol;

}
