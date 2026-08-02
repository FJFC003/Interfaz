package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.integrador.InformeCompletoIRRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.InformeResultadosIRResponseDto;

public interface IInformeResultadosIRService {
	
	List<InformeResultadosIRResponseDto> listarInformes();
	InformeResultadosIRResponseDto buscarPorId(int idInforme);
	InformeResultadosIRResponseDto buscarPorOrden(int idOT);
	void eliminar(int idInforme);

	InformeResultadosIRResponseDto guardarCompleto(InformeCompletoIRRequestDto dto);

}
