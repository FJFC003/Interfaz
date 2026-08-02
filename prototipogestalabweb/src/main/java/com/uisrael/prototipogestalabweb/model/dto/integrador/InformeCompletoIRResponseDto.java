package com.uisrael.prototipogestalabweb.model.dto.integrador;

import java.util.ArrayList;
import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.response.CondicionAmbientalIRResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EquiposUtilizadosIRResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.InformeResultadosIRResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.ResultadosIRResponseDto;

import lombok.Data;

@Data
public class InformeCompletoIRResponseDto {
	
	private InformeResultadosIRResponseDto informe;

	private List<ResultadosIRResponseDto> listaResultados = new ArrayList<>();

	private List<CondicionAmbientalIRResponseDto> listaCondiciones = new ArrayList<>();

	private List<EquiposUtilizadosIRResponseDto> listaEquipos = new ArrayList<>();

}
