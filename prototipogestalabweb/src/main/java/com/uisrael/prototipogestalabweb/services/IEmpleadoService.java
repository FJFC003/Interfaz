package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.EmpleadoRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EmpleadoResponseDto;

public interface IEmpleadoService {
	
	List<EmpleadoResponseDto> listarEmpleados();
	void guardarEmpleados(EmpleadoRequestDto empleado);
	EmpleadoResponseDto buscarPorId(int idEmpleado);
	void eliminarEmpleado(int idEmpleado);

}
