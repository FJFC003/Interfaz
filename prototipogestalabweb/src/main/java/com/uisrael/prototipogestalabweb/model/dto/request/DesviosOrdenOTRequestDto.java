package com.uisrael.prototipogestalabweb.model.dto.request;

import lombok.Data;

@Data
public class DesviosOrdenOTRequestDto {
	
	private int idDesviosOrdenOT;
	private int noItemDesviosOrdenOT;
	private String descripcionDesviosOrdenOT;
	private int puntosModificadosDesviosOrdenOT;
	private int fkOrdenTrabajo;

}
