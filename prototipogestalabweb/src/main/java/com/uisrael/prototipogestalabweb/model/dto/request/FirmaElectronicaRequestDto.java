package com.uisrael.prototipogestalabweb.model.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class FirmaElectronicaRequestDto {
	
	private int idFirma;
	private String formatoFirma;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaSubida;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaExpiracion;

}
