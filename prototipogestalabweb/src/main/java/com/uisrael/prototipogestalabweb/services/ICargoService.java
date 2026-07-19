package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.CargoRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CargoResponseDto;

public interface ICargoService {
	
	List<CargoResponseDto> listarCargos();
	void guardarCargos(CargoRequestDto cargo);

}
