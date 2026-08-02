package com.uisrael.prototipogestalabweb.controller;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import com.uisrael.prototipogestalabweb.model.dto.integrador.InformeCompletoIRRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.integrador.InformeCompletoIRResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.request.CondicionAmbientalIRRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.EquiposUtilizadosIRRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.ResultadosIRRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CondicionAmbientalIRResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EquipoLaboratorioResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EquiposUtilizadosIRResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.InformeResultadosIRResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.OrdenTrabajoOTResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.ParametroAnalizarPLResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.ResultadosIRResponseDto;
import com.uisrael.prototipogestalabweb.services.IEquipoLaboratorioService;
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
	private final IEquipoLaboratorioService equipoService;
	private final SpringTemplateEngine templateEngine;
	
	public InformeResultadosIRController(IInformeResultadosIRService informeService,
			IOrdenTrabajoOTService ordenService, IParametroAnalizarPLService parametroService,
			IEquipoLaboratorioService equipoService, SpringTemplateEngine templateEngine) {
		super();
		this.informeService = informeService;
		this.ordenService = ordenService;
		this.parametroService = parametroService;
		this.equipoService = equipoService;
		this.templateEngine = templateEngine;
	}
	
	@GetMapping("/bandeja")
	public String bandeja(Model model) {
		try {
			// Solo las ordenes que la Coordinacion Tecnica envio al laboratorio.
			List<OrdenTrabajoOTResponseDto> trabajos = ordenService.listarParaLaboratorio();
			model.addAttribute("trabajos", trabajos);
			model.addAttribute("ordenesConInforme", obtenerOrdenesConInforme());
			return "informe/bandejalaboratorio";
		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	
	/**
	 * Catalogo de equipos vigentes para el desplegable del informe.
	 * Si el catalogo falla, la pantalla debe abrirse igual y el tecnico
	 * podra escribir los equipos a mano.
	 */
	private List<EquipoLaboratorioResponseDto> obtenerEquiposActivos() {
		try {
			List<EquipoLaboratorioResponseDto> equipos = equipoService.listarActivos();
			return equipos != null ? equipos : new ArrayList<>();
		} catch (Exception e) {
			return new ArrayList<>();
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

			InformeCompletoIRResponseDto guardado = buscarInformeGuardado(idOT);
			boolean yaExiste = guardado != null && guardado.getInforme() != null;

			InformeCompletoIRRequestDto form = yaExiste
					? reconstruirDesdeGuardado(guardado, idOT)
					: crearFormularioNuevo(orden, idOT);

			model.addAttribute("informeCompleto", form);
			model.addAttribute("orden", orden);
			model.addAttribute("equiposCatalogo", obtenerEquiposActivos());
			model.addAttribute("informeGuardado", yaExiste);
			model.addAttribute("informeEnviado", yaExiste
					&& "ENVIADO_COORDINACION".equals(guardado.getInforme().getEstadoInforme()));
			return "informe/informeresultado";

		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	/**
	 * Devuelve el informe ya guardado de una orden, o null si todavia no existe.
	 * El backend responde 204 cuando no hay informe, asi que un cuerpo vacio es
	 * una respuesta valida y no un error.
	 */
	private InformeCompletoIRResponseDto buscarInformeGuardado(int idOT) {
		try {
			return informeService.buscarCompletoPorOrden(idOT);
		} catch (Exception e) {
			return null;
		}
	}

	/** Formulario en blanco, con los parametros precargados del Plan de Muestreo. */
	private InformeCompletoIRRequestDto crearFormularioNuevo(OrdenTrabajoOTResponseDto orden, int idOT) {

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

		form.setListaResultados(rellenarResultados(resultados));
		form.setListaCondiciones(rellenarCondiciones(new ArrayList<>()));
		form.setListaEquipos(rellenarEquipos(new ArrayList<>()));
		return form;
	}

	/** Vuelve a armar el formulario con lo que el tecnico ya habia guardado. */
	private InformeCompletoIRRequestDto reconstruirDesdeGuardado(InformeCompletoIRResponseDto guardado, int idOT) {

		InformeCompletoIRRequestDto form = new InformeCompletoIRRequestDto();
		InformeResultadosIRResponseDto cabecera = guardado.getInforme();

		form.getInforme().setIdInforme(cabecera.getIdInforme());
		form.getInforme().setCodigoInforme(cabecera.getCodigoInforme());
		form.getInforme().setFechaEmisionInforme(cabecera.getFechaEmisionInforme());
		form.getInforme().setNotasAdvertencia(cabecera.getNotasAdvertencia());
		form.getInforme().setNombreResponsable(cabecera.getNombreResponsable());
		form.getInforme().setConformidadGeneral(cabecera.getConformidadGeneral());
		form.getInforme().setFkOrdenTrabajo(idOT);
		if (cabecera.getFkDatosLaboratorio() != null) {
			form.getInforme().setFkDatosLaboratorio(cabecera.getFkDatosLaboratorio().getIdDatos());
		}

		List<ResultadosIRRequestDto> resultados = new ArrayList<>();
		for (ResultadosIRResponseDto r : guardado.getListaResultados()) {
			ResultadosIRRequestDto fila = new ResultadosIRRequestDto();
			fila.setIdResultados(r.getIdResultados());
			fila.setNoItem(r.getNoItem());
			fila.setParametros(r.getParametros());
			fila.setMetodoReferencial(r.getMetodoReferencial());
			fila.setUnidad(r.getUnidad());
			fila.setResultado(r.getResultado());
			fila.setIncertidumbre(r.getIncertidumbre());
			fila.setLMP(r.getLMP());
			fila.setConformidad(r.getConformidad());
			resultados.add(fila);
		}
		form.setListaResultados(rellenarResultados(resultados));

		List<CondicionAmbientalIRRequestDto> condiciones = new ArrayList<>();
		for (CondicionAmbientalIRResponseDto c : guardado.getListaCondiciones()) {
			CondicionAmbientalIRRequestDto fila = new CondicionAmbientalIRRequestDto();
			fila.setIdCondi(c.getIdCondi());
			fila.setNoAlicuota(c.getNoAlicuota());
			fila.setHoraToma(c.getHoraToma());
			fila.setTemperatura(c.getTemperatura());
			fila.setHumedad(c.getHumedad());
			fila.setPrecipitacion(c.getPrecipitacion());
			condiciones.add(fila);
		}
		form.setListaCondiciones(rellenarCondiciones(condiciones));

		List<EquiposUtilizadosIRRequestDto> equipos = new ArrayList<>();
		for (EquiposUtilizadosIRResponseDto e : guardado.getListaEquipos()) {
			EquiposUtilizadosIRRequestDto fila = new EquiposUtilizadosIRRequestDto();
			fila.setIdEquipos(e.getIdEquipos());
			fila.setNombre(e.getNombre());
			fila.setMarca(e.getMarca());
			fila.setModelo(e.getModelo());
			fila.setSerie(e.getSerie());
			fila.setCodigoInterno(e.getCodigoInterno());
			equipos.add(fila);
		}
		form.setListaEquipos(rellenarEquipos(equipos));

		return form;
	}

	// Filas vacias extra, para que el tecnico siempre pueda agregar mas lineas.
	private List<ResultadosIRRequestDto> rellenarResultados(List<ResultadosIRRequestDto> lista) {
		while (lista.size() < FILAS_RESULTADOS) {
			lista.add(new ResultadosIRRequestDto());
		}
		return lista;
	}

	private List<CondicionAmbientalIRRequestDto> rellenarCondiciones(List<CondicionAmbientalIRRequestDto> lista) {
		while (lista.size() < FILAS_CONDICIONES) {
			lista.add(new CondicionAmbientalIRRequestDto());
		}
		return lista;
	}

	private List<EquiposUtilizadosIRRequestDto> rellenarEquipos(List<EquiposUtilizadosIRRequestDto> lista) {
		while (lista.size() < FILAS_EQUIPOS) {
			lista.add(new EquiposUtilizadosIRRequestDto());
		}
		return lista;
	}

	@PostMapping("/guardar/{idOT}")
	public String guardar(@PathVariable int idOT,
			@ModelAttribute("informeCompleto") InformeCompletoIRRequestDto informeCompleto,
			Model model) {
		try {
			informeCompleto.getInforme().setFkOrdenTrabajo(idOT);
			informeService.guardarCompleto(informeCompleto);
			return "redirect:/informe/resultados/" + idOT + "?success=true";

		} catch (WebClientResponseException ex) {
			model.addAttribute("mensajeError", ex.getResponseBodyAsString());
			return "error";
		} catch (Exception ex) {
			model.addAttribute("mensajeError",
					ex.getClass().getSimpleName() + " - " + ex.getMessage());
			return "error";
		}
	}

	/**
	 * Genera el PDF del informe: renderiza la plantilla informe/informepdf con
	 * Thymeleaf y convierte ese HTML a PDF con openhtmltopdf.
	 */
	@GetMapping("/pdf/{idOT}")
	public ResponseEntity<byte[]> descargarPdf(@PathVariable int idOT) {
		try {
			InformeCompletoIRResponseDto guardado = informeService.buscarCompletoPorOrden(idOT);
			if (guardado == null || guardado.getInforme() == null) {
				return ResponseEntity.notFound().build();
			}

			OrdenTrabajoOTResponseDto orden = ordenService.buscarPorId(idOT);

			Context contexto = new Context(new Locale("es", "EC"));
			contexto.setVariable("orden", orden);
			contexto.setVariable("informe", guardado.getInforme());
			contexto.setVariable("resultados", guardado.getListaResultados());
			contexto.setVariable("condiciones", guardado.getListaCondiciones());
			contexto.setVariable("equipos", guardado.getListaEquipos());

			String html = templateEngine.process("informe/informepdf", contexto);

			ByteArrayOutputStream salida = new ByteArrayOutputStream();
			PdfRendererBuilder constructor = new PdfRendererBuilder();
			constructor.useFastMode();
			constructor.withHtmlContent(html, null);
			constructor.toStream(salida);
			constructor.run();

			String codigo = guardado.getInforme().getCodigoInforme();
			String nombreArchivo = "Informe-" + (codigo != null ? codigo : idOT) + ".pdf";

			HttpHeaders cabeceras = new HttpHeaders();
			cabeceras.setContentType(MediaType.APPLICATION_PDF);
			cabeceras.set(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=\"" + nombreArchivo + "\"");

			return new ResponseEntity<>(salida.toByteArray(), cabeceras, HttpStatus.OK);

		} catch (Exception e) {
			byte[] mensaje = ("No se pudo generar el PDF: " + e.getMessage())
					.getBytes(StandardCharsets.UTF_8);
			return ResponseEntity.internalServerError()
					.contentType(MediaType.TEXT_PLAIN).body(mensaje);
		}
	}

	// ================= FLUJO HACIA LA COORDINACION TECNICA =================

	/**
	 * El Tecnico de Laboratorio da por terminado el informe y lo envia a la
	 * Coordinacion Tecnica. Desde ese momento la coordinadora puede verlo.
	 */
	@PostMapping("/enviar/{idOT}")
	public String enviarACoordinacion(@PathVariable int idOT, Model model) {
		try {
			InformeCompletoIRResponseDto guardado = informeService.buscarCompletoPorOrden(idOT);
			if (guardado == null || guardado.getInforme() == null) {
				model.addAttribute("mensajeError",
						"Primero debe guardar el informe antes de enviarlo a Coordinacion.");
				return "error";
			}

			informeService.enviarACoordinacion(guardado.getInforme().getIdInforme());
			return "redirect:/informe/resultados/" + idOT + "?enviado=true";

		} catch (WebClientResponseException ex) {
			model.addAttribute("mensajeError", ex.getResponseBodyAsString());
			return "error";
		} catch (Exception ex) {
			model.addAttribute("mensajeError", ex.getMessage());
			return "error";
		}
	}

	/** Bandeja de la Coordinacion Tecnica: informes recibidos, solo lectura. */
	@GetMapping("/coordinacion")
	public String bandejaCoordinacion(Model model) {
		try {
			model.addAttribute("informes", informeService.listarEnviados());
			return "informe/bandejacoordinacion";
		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

	/**
	 * Vista de solo lectura del informe. No tiene formulario ni boton de guardar:
	 * la Coordinacion Tecnica consulta y descarga, no edita.
	 */
	@GetMapping("/ver/{idOT}")
	public String verInforme(@PathVariable int idOT, Model model) {
		try {
			InformeCompletoIRResponseDto guardado = informeService.buscarCompletoPorOrden(idOT);
			if (guardado == null || guardado.getInforme() == null) {
				model.addAttribute("mensajeError", "Esa orden todavia no tiene informe emitido.");
				return "error";
			}

			model.addAttribute("orden", ordenService.buscarPorId(idOT));
			model.addAttribute("informe", guardado.getInforme());
			model.addAttribute("resultados", guardado.getListaResultados());
			model.addAttribute("condiciones", guardado.getListaCondiciones());
			model.addAttribute("equipos", guardado.getListaEquipos());
			return "informe/verinforme";

		} catch (Exception e) {
			model.addAttribute("mensajeError", e.getMessage());
			return "error";
		}
	}

}