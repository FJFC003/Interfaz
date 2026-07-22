package com.uisrael.prototipogestalabweb.model.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class FirmaElectronicaResponseDto {
	
	private int idFirma;
	private String formatoFirma;
	private Date fechaSubida;
	private Date fechaExpiracion;

}
