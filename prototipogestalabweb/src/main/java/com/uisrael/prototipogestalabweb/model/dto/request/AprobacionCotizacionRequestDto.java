package com.uisrael.prototipogestalabweb.model.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class AprobacionCotizacionRequestDto {

	private Integer fkEmpleadoAprueba;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaPago;

}
