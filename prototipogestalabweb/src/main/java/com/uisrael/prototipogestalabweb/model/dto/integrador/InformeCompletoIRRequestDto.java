package com.uisrael.prototipogestalabweb.model.dto.integrador;

import lombok.Data;

@Data
public class InformeCompletoIRRequestDto {
	
	private InformeResultadosIRRequestDto informe = new InformeResultadosIRRequestDto();
	private List<ResultadosIRRequestDto> listaResultados = new ArrayList<>();
	private List<CondicionAmbientalIRRequestDto> listaCondiciones = new ArrayList<>();
	private List<EquiposUtilizadosIRRequestDto> listaEquipos = new ArrayList<>();

}
