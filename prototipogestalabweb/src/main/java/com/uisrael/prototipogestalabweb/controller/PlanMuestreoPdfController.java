package com.uisrael.prototipogestalabweb.controller;

import java.io.ByteArrayOutputStream;

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
import com.uisrael.prototipogestalabweb.model.dto.response.PlanMuestreoPLResponseDto;
import com.uisrael.prototipogestalabweb.services.IInformacionAdicionalPLService;
import com.uisrael.prototipogestalabweb.services.IInformacionMatrizPLService;
import com.uisrael.prototipogestalabweb.services.IParametroAnalizarPLService;
import com.uisrael.prototipogestalabweb.services.IPlanMuestreoPLService;
import com.uisrael.prototipogestalabweb.services.IProcedimientoMuePLService;
import com.uisrael.prototipogestalabweb.services.IRecursosCronoPLService;
import com.uisrael.prototipogestalabweb.services.ITipoTomaFreHoraPLService;
import com.uisrael.prototipogestalabweb.services.IVerificacionPLService;

@Controller
@RequestMapping("/plan")
public class PlanMuestreoPdfController {
	
	private final IPlanMuestreoPLService planService;
	private final IInformacionMatrizPLService matrizService;
	private final IParametroAnalizarPLService parametroService;
	private final ITipoTomaFreHoraPLService tipoTomaService;
	private final IProcedimientoMuePLService procedimientoService;
	private final IRecursosCronoPLService recursosService;
	private final IInformacionAdicionalPLService infoAdicionalService;
	private final IVerificacionPLService verificacionService;
	private final SpringTemplateEngine templateEngine;
	
	public PlanMuestreoPdfController(IPlanMuestreoPLService planService, IInformacionMatrizPLService matrizService,
			IParametroAnalizarPLService parametroService, ITipoTomaFreHoraPLService tipoTomaService,
			IProcedimientoMuePLService procedimientoService, IRecursosCronoPLService recursosService,
			IInformacionAdicionalPLService infoAdicionalService, IVerificacionPLService verificacionService,
			SpringTemplateEngine templateEngine) {
		super();
		this.planService = planService;
		this.matrizService = matrizService;
		this.parametroService = parametroService;
		this.tipoTomaService = tipoTomaService;
		this.procedimientoService = procedimientoService;
		this.recursosService = recursosService;
		this.infoAdicionalService = infoAdicionalService;
		this.verificacionService = verificacionService;
		this.templateEngine = templateEngine;
	}
	
	@GetMapping("/pdf/{idPlan}")
	public ResponseEntity<byte[]> generarPdf(@PathVariable int idPlan) {
		try {
			PlanMuestreoPLResponseDto plan = planService.buscarPorId(idPlan);

			Context context = new Context();
			context.setVariable("plan", plan);
			context.setVariable("matrices", matrizService.listarPorPlan(idPlan));
			context.setVariable("parametros", parametroService.listarPorPlan(idPlan));
			context.setVariable("tiposToma", tipoTomaService.listarPorPlan(idPlan));
			context.setVariable("procedimientos", procedimientoService.listarPorPlan(idPlan));
			context.setVariable("recursos", recursosService.listarPorPlan(idPlan));
			context.setVariable("infoAdicional", infoAdicionalService.listarPorPlan(idPlan));
			context.setVariable("verificaciones", verificacionService.listarPorPlan(idPlan));

			String html = templateEngine.process("plan/pdfplan", context);

			ByteArrayOutputStream salida = new ByteArrayOutputStream();
			PdfRendererBuilder builder = new PdfRendererBuilder();
			builder.useFastMode();
			builder.withHtmlContent(html, null);
			builder.toStream(salida);
			builder.run();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			String nombre = "plan-muestreo-"
					+ (plan.getCodigoPlan() != null ? plan.getCodigoPlan().replace("/", "-") : idPlan) + ".pdf";
			headers.setContentDisposition(ContentDisposition.attachment().filename(nombre).build());

			return new ResponseEntity<>(salida.toByteArray(), headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
