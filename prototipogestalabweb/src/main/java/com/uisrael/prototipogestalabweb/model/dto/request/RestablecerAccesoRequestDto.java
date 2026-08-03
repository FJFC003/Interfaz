package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class RestablecerAccesoRequestDto {
	
	private String correo;
	private String respuesta;
	private String nuevaContrasenia;

	/** Solo se usa en la pantalla para confirmar; no se envia al backend. */
	private String confirmacionContrasenia;

}
