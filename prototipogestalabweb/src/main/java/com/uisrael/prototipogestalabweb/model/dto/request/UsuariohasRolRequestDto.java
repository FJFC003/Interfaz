package com.uisrael.prototipogestalabweb.model.dto.request;

import java.util.Date;

import lombok.Data;

@Data
public class UsuariohasRolRequestDto {
	
	private int idUsuarioRol;
	private Date fechaAsignacion;
	private int fkUsuario;
	private int fkRol;

}
