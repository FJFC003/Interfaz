package com.uisrael.prototipogestalabweb.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.uisrael.prototipogestalabweb.model.dto.integrador.InformeCompletoIRRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.CondicionAmbientalIRRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.EquiposUtilizadosIRRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.ResultadosIRRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.InformeResultadosIRResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.OrdenTrabajoOTResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.ParametroAnalizarPLResponseDto;
import com.uisrael.prototipogestalabweb.services.IInformeResultadosIRService;
import com.uisrael.prototipogestalabweb.services.IOrdenTrabajoOTService;
import com.uisrael.prototipogestalabweb.services.IParametroAnalizarPLService;

@Controller
@RequestMapping("/informe")
public class InformeResultadosIRController {
	
	private static final int FILAS_RESULTADOS = 8;
	private static final int FILAS_CONDICIONES = 3;
	private static final int FILAS_EQUIPOS = 3;

	private final IInformeResultadosIRService informeService;
	private final IOrdenTrabajoOTService ordenService;
	private final IParametroAnalizarPLService parametroService;
	public InformeResultadosIRController(IInformeResultadosIRService informeService,
			IOrdenTrabajoOTService ordenService, IParametroAnalizarPLService parametroService) {
		super();
		this.informeService = informeService;
		this.ordenService = ordenService;
		this.parametroService = parametroService;
	}
	
		@GetMapping("/bandeja")
		public String bandeja(Model model) {
			try {
				List<OrdenTrabajoOTResponseDto> trabajos = ordenService.listarOrdenes();
				model.addAttribute("trabajos", trabajos);
				model.addAttribute("ordenesConInforme", obtenerOrdenesConInforme());
				return "informe/bandejalaboratorio";
			} catch (Exception e) {
				model.addAttribute("mensajeError", e.getMessage());
				return "error";
			}
		}

		
		private List<Integer> obtenerOrdenesConInforme() {
			List<Integer> ids = new ArrayList<>();
			try {
				List<InformeResultadosIRResponseDto> informes = informeService.listarInformes();
				if (informes != null) {
					for (InformeResultadosIRResponseDto informe : informes) {
						if (informe.getFkOrdenTrabajo() != null) {
							ids.add(informe.getFkOrdenTrabajo().getIdOT());
						}
					}
				}
			} catch (Exception e) {
				// La bandeja debe mostrarse aunque el listado de informes falle.
			}
			return ids;
		}

		@GetMapping("/resultados/{idOT}")
		public String mostrarFormulario(@PathVariable int idOT, Model model) {
			try {
				OrdenTrabajoOTResponseDto orden = ordenService.buscarPorId(idOT);

				InformeCompletoIRRequestDto form = new InformeCompletoIRRequestDto();
				form.getInforme().setFkOrdenTrabajo(idOT);
				form.getInforme().setFechaEmisionInforme(new Date());
				form.getInforme().setCodigoInforme("IR-" + idOT);

				List<ResultadosIRRequestDto> resultados = new ArrayList<>();
				if (orden.getFkPlanMuestreo() != null) {
					for (ParametroAnalizarPLResponseDto p :
							parametroService.listarPorPlan(orden.getFkPlanMuestreo().getIdPlan())) {
						ResultadosIRRequestDto fila = new ResultadosIRRequestDto();
						fila.setParametros(p.getParametros());
						fila.setUnidad(p.getUnidadMedida());
						resultados.add(fila);
					}
				}
				while (resultados.size() < FILAS_RESULTADOS) {
					resultados.add(new ResultadosIRRequestDto());
				}
				form.setListaResultados(resultados);

				List<CondicionAmbientalIRRequestDto> condiciones = new ArrayList<>();
				for (int i = 0; i < FILAS_CONDICIONES; i++) {
					condiciones.add(new CondicionAmbientalIRRequestDto());
				}
				form.setListaCondiciones(condiciones);

				List<EquiposUtilizadosIRRequestDto> equipos = new ArrayList<>();
				for (int i = 0; i < FILAS_EQUIPOS; i++) {
					equipos.add(new EquiposUtilizadosIRRequestDto());
				}
				form.setListaEquipos(equipos);

				model.addAttribute("informeCompleto", form);
				model.addAttribute("orden", orden);
				return "informe/informeresultado";

			} catch (Exception e) {
				model.addAttribute("mensajeError", e.getMessage());
				return "error";
			}
		}

		@PostMapping("/guardar/{idOT}")
		public String guardar(@PathVariable int idOT,
				@ModelAttribute("informeCompleto") InformeCompletoIRRequestDto informeCompleto,
				Model model) {
			try {
				informeCompleto.getInforme().setFkOrdenTrabajo(idOT);
				informeService.guardarCompleto(informeCompleto);
				return "redirect:/informe/bandeja?success=true";

			} catch (WebClientResponseException ex) {
				model.addAttribute("mensajeError", ex.getResponseBodyAsString());
				return "error";
			} catch (Exception ex) {
				model.addAttribute("mensajeError",
						ex.getClass().getSimpleName() + " - " + ex.getMessage());
				return "error";
			}
		}

}
