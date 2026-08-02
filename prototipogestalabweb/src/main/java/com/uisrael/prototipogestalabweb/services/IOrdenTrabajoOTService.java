package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.OrdenTrabajoOTRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.OrdenTrabajoOTResponseDto;

public interface IOrdenTrabajoOTService {

	List<OrdenTrabajoOTResponseDto> listarOrdenes();
	OrdenTrabajoOTResponseDto guardar(OrdenTrabajoOTRequestDto dto);
	OrdenTrabajoOTResponseDto buscarPorId(int idOT);
	void eliminar(int idOT);
	List<OrdenTrabajoOTResponseDto> listarPorTecnico(int idEmpleado);
	List<OrdenTrabajoOTResponseDto> listarPorPlan(int idPlan);

	OrdenTrabajoOTResponseDto enviarALaboratorio(int idOT);
	OrdenTrabajoOTResponseDto devolverACoordinacion(int idOT);
	List<OrdenTrabajoOTResponseDto> listarParaLaboratorio();

}
