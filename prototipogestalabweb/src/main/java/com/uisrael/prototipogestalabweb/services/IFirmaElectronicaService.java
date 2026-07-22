package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.FirmaElectronicaRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.FirmaElectronicaResponseDto;

public interface IFirmaElectronicaService {
	
	List<FirmaElectronicaResponseDto> listarFirmas();
	FirmaElectronicaResponseDto guardarFirmas(FirmaElectronicaRequestDto firma);

}
