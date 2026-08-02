package com.uisrael.prototipogestalabweb.model.dto.integrador;

import java.util.ArrayList;
import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.CondicionAmbientalIRRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.EquiposUtilizadosIRRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.InformeResultadosIRRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.ResultadosIRRequestDto;

import lombok.Data;

@Data
public class InformeCompletoIRRequestDto {
	
	private InformeResultadosIRRequestDto informe = new InformeResultadosIRRequestDto();
	private List<ResultadosIRRequestDto> listaResultados = new ArrayList<>();
	private List<CondicionAmbientalIRRequestDto> listaCondiciones = new ArrayList<>();
	private List<EquiposUtilizadosIRRequestDto> listaEquipos = new ArrayList<>();

}
