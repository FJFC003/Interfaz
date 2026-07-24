package com.uisrael.prototipogestalabweb.model.dto.response;

import lombok.Data;

@Data
public class ClienteCResponseDto {
	
	private int idClienteC;
	private String tipoClienteC;
	private String ciClienteC;
	private String nombreRazonSocialClienteC;
	private String nombrePersonaContactoClienteC;
	private String direccionClienteC;
	private String telefonoClienteC;
	private String correoClienteC;
	private boolean estadoClienteC;

}
