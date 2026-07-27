package com.uisrael.prototipogestalabweb.controller;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.uisrael.prototipogestalabweb.model.dto.response.CotizacionCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DetalleCResponseDto;
import com.uisrael.prototipogestalabweb.services.ICotizacionCService;
import com.uisrael.prototipogestalabweb.services.IDetalleCService;

/**
 * Genera el PDF de una Cotización con el formato del Excel de referencia
 * (F-PP-07-01). Esta primera versión usa una sola tabla combinada con todas
 * las líneas de detalle, sin separarlas por categoría (Agua/Ruido/etc.).
 */
@Controller
@RequestMapping("/cotizacion")
public class CotizacionPdfController {

	private static final List<String> ORDEN_COMPONENTES = List.of("AGUA", "RUIDO", "EMISIONES", "CALIDAD DEL AIRE",
			"SUELO");

	private final ICotizacionCService cotizacionService;
	private final IDetalleCService detalleService;
	private final SpringTemplateEngine templateEngine;

	public CotizacionPdfController(ICotizacionCService cotizacionService, IDetalleCService detalleService,
			SpringTemplateEngine templateEngine) {
		super();
		this.cotizacionService = cotizacionService;
		this.detalleService = detalleService;
		this.templateEngine = templateEngine;
	}

	@GetMapping("/pdf/{id}")
	public ResponseEntity<byte[]> generarPdf(@PathVariable int id) {
		try {
			CotizacionCResponseDto cotizacion = cotizacionService.buscarPorId(id);
			List<DetalleCResponseDto> detalles = detalleService.listarPorCotizacion(id);

			Context context = new Context();
			context.setVariable("cotizacion", cotizacion);
			context.setVariable("detalles", detalles);
			context.setVariable("numeroCotizacion", generarNumeroCotizacion(cotizacion));

			byte[] pdf = renderizarPdf("cotizacion/pdfcotizacion", context);
			return respuestaPdf(pdf, "cotizacion-" + id + ".pdf");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Versión "fiel": agrupa las líneas de detalle por Componente
	// (AGUA/RUIDO/EMISIONES/CALIDAD DEL AIRE/SUELO), como en el Excel original.
	@GetMapping("/pdf-fiel/{id}")
	public ResponseEntity<byte[]> generarPdfFiel(@PathVariable int id) {
		try {
			CotizacionCResponseDto cotizacion = cotizacionService.buscarPorId(id);
			List<DetalleCResponseDto> detalles = detalleService.listarPorCotizacion(id);

			Map<String, List<DetalleCResponseDto>> porComponente = new LinkedHashMap<>();
			for (String componente : ORDEN_COMPONENTES) {
				porComponente.put(componente, new java.util.ArrayList<>());
			}
			porComponente.put("SIN COMPONENTE ASIGNADO", new java.util.ArrayList<>());

			for (DetalleCResponseDto det : detalles) {
				String componente = (det.getFkParametro() != null && det.getFkParametro().getComponenteParametroC() != null
						&& !det.getFkParametro().getComponenteParametroC().isBlank())
								? det.getFkParametro().getComponenteParametroC().toUpperCase()
								: "SIN COMPONENTE ASIGNADO";
				porComponente.computeIfAbsent(componente, k -> new java.util.ArrayList<>()).add(det);
			}
			// Quita categorías sin ninguna línea, para no imprimir tablas vacías.
			porComponente.entrySet().removeIf(e -> e.getValue().isEmpty());

			Context context = new Context();
			context.setVariable("cotizacion", cotizacion);
			context.setVariable("detallesPorComponente", porComponente);
			context.setVariable("numeroCotizacion", generarNumeroCotizacion(cotizacion));

			byte[] pdf = renderizarPdf("cotizacion/pdfcotizacionfiel", context);
			return respuestaPdf(pdf, "cotizacion-" + id + "-fiel.pdf");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private byte[] renderizarPdf(String nombrePlantilla, Context context) throws Exception {
		String html = templateEngine.process(nombrePlantilla, context);

		ByteArrayOutputStream salida = new ByteArrayOutputStream();
		PdfRendererBuilder builder = new PdfRendererBuilder();
		builder.useFastMode();
		builder.withHtmlContent(html, null);
		builder.toStream(salida);
		builder.run();
		return salida.toByteArray();
	}

	private ResponseEntity<byte[]> respuestaPdf(byte[] pdf, String nombreArchivo) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDisposition(ContentDisposition.attachment().filename(nombreArchivo).build());
		return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
	}

	private String generarNumeroCotizacion(CotizacionCResponseDto cotizacion) {
		Date fecha = cotizacion.getFechaElaboracionCotizacionC() != null ? cotizacion.getFechaElaboracionCotizacionC()
				: new Date();
		String anio = new SimpleDateFormat("yyyy").format(fecha);
		return "LGA-PS-" + anio + "-" + cotizacion.getIdCotizacionC();
	}
}