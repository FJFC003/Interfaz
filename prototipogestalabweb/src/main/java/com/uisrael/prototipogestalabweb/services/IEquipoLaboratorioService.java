package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.EquipoLaboratorioRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EquipoLaboratorioResponseDto;

public interface IEquipoLaboratorioService {
	
	List<EquipoLaboratorioResponseDto> listarEquipos();
	List<EquipoLaboratorioResponseDto> listarActivos();
	EquipoLaboratorioResponseDto buscarPorId(int idEquipoLab);
	void guardarEquipo(EquipoLaboratorioRequestDto equipo);
	void eliminarEquipo(int idEquipoLab);

}
