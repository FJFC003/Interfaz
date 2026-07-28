package com.uisrael.prototipogestalabweb.services;

import java.util.List;

import com.uisrael.prototipogestalabweb.model.dto.request.PlanMuestreoPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.PlanMuestreoPLResponseDto;

public interface IPlanMuestreoPLService {

	List<PlanMuestreoPLResponseDto> listarPlanes();
	PlanMuestreoPLResponseDto guardar(PlanMuestreoPLRequestDto dto);
	PlanMuestreoPLResponseDto buscarPorId(int idPlan);
	void eliminar(int idPlan);
	List<PlanMuestreoPLResponseDto> listarPorCotizacion(int idCotizacionC);
	List<PlanMuestreoPLResponseDto> listarPorDetalle(int idDetalleC);
	List<PlanMuestreoPLResponseDto> listarPorResponsable(int idEmpleado);

	PlanMuestreoPLResponseDto enviarATecnico(int idPlan);
	PlanMuestreoPLResponseDto devolverAElaboracion(int idPlan);
	PlanMuestreoPLResponseDto marcarCompletado(int idPlan);

}
