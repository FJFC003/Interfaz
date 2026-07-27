package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.RecursosCronoPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.RecursosCronoPLResponseDto;

public interface IRecursosCronoPLService {

	List<RecursosCronoPLResponseDto> listarRecursos();
	RecursosCronoPLResponseDto guardar(RecursosCronoPLRequestDto dto);
	void eliminar(int idRecursos);
	List<RecursosCronoPLResponseDto> listarPorPlan(int idPlan);

}
