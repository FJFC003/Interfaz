package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.integrador.InformeCompletoIRRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.integrador.InformeCompletoIRResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.InformeResultadosIRResponseDto;

public interface IInformeResultadosIRService {
	
	List<InformeResultadosIRResponseDto> listarInformes();
	InformeResultadosIRResponseDto buscarPorId(int idInforme);
	InformeResultadosIRResponseDto buscarPorOrden(int idOT);
	InformeCompletoIRResponseDto buscarCompletoPorOrden(int idOT);

	InformeResultadosIRResponseDto enviarACoordinacion(int idInforme);
	List<InformeResultadosIRResponseDto> listarEnviados();
	void eliminar(int idInforme);

	InformeResultadosIRResponseDto guardarCompleto(InformeCompletoIRRequestDto dto);


}
