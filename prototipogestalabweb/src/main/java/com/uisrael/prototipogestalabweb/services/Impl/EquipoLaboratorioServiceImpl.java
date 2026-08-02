package com.uisrael.prototipogestalabweb.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.prototipogestalabweb.model.dto.request.EquipoLaboratorioRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EquipoLaboratorioResponseDto;
import com.uisrael.prototipogestalabweb.services.IEquipoLaboratorioService;

@Service
public class EquipoLaboratorioServiceImpl implements IEquipoLaboratorioService {

	private final WebClient webClient;

	public EquipoLaboratorioServiceImpl(WebClient webClient) {
		super();
		this.webClient = webClient;
	}

	@Override
	public List<EquipoLaboratorioResponseDto> listarEquipos() {
		return webClient.get().uri("/gestalab/equipolaboratorio")
				.retrieve().bodyToFlux(EquipoLaboratorioResponseDto.class).collectList().block();
	}

	@Override
	public List<EquipoLaboratorioResponseDto> listarActivos() {
		return webClient.get().uri("/gestalab/equipolaboratorio/activos")
				.retrieve().bodyToFlux(EquipoLaboratorioResponseDto.class).collectList().block();
	}

	@Override
	public EquipoLaboratorioResponseDto buscarPorId(int idEquipoLab) {
		return webClient.get().uri("/gestalab/equipolaboratorio/{id}", idEquipoLab)
				.retrieve().bodyToMono(EquipoLaboratorioResponseDto.class).block();
	}

	@Override
	public void guardarEquipo(EquipoLaboratorioRequestDto equipo) {
		webClient.post().uri("/gestalab/equipolaboratorio")
				.bodyValue(equipo).retrieve().toBodilessEntity().block();
	}

	@Override
	public void eliminarEquipo(int idEquipoLab) {
		webClient.delete().uri("/gestalab/equipolaboratorio/{id}", idEquipoLab)
				.retrieve().toBodilessEntity().block();
	}

}