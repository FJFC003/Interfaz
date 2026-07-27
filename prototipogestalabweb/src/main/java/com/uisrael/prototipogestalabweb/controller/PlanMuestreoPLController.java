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

import com.uisrael.prototipogestalabweb.model.dto.request.InformacionAdicionalPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.InformacionMatrizPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.ParametroAnalizarPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.PlanMuestreoPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.ProcedimientoMuePLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.RecursosCronoPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.TipoTomaFreHoraPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.request.VerificacionPLRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.CotizacionCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.DetalleCResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.EmpleadoResponseDto;
import com.uisrael.prototipogestalabweb.model.dto.response.PlanMuestreoPLResponseDto;
import com.uisrael.prototipogestalabweb.services.ICotizacionCService;
import com.uisrael.prototipogestalabweb.services.IDetalleCService;
import com.uisrael.prototipogestalabweb.services.IEmpleadoService;
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
public class PlanMuestreoPLController {

	private final IPlanMuestreoPLService planService;
	private final IInformacionMatrizPLService matrizService;
	private final IParametroAnalizarPLService parametroService;
	private final ITipoTomaFreHoraPLService tipoTomaService;
	private final IProcedimientoMuePLService procedimientoService;
	private final IRecursosCronoPLService recursosService;
	private final IInformacionAdicionalPLService infoAdicionalService;
	private final IVerificacionPLService verificacionService;
	private final ICotizacionCService cotizacionService;
	private final IDetalleCService detalleService;
	private final IEmpleadoService empleadoService;

	public PlanMuestreoPLController(IPlanMuestreoPLService planService,
			IInformacionMatrizPLService matrizService, IParametroAnalizarPLService parametroService,
			ITipoTomaFreHoraPLService tipoTomaService, IProcedimientoMuePLService procedimientoService,
			IRecursosCronoPLService recursosService, IInformacionAdicionalPLService infoAdicionalService,
			IVerificacionPLService verificacionService, ICotizacionCService cotizacionService,
			IDetalleCService detalleService, IEmpleadoService empleadoService) {
		this.planService = planService;
		this.matrizService = matrizService;
		this.parametroService = parametroService;
		this.tipoTomaService = tipoTomaService;
		this.procedimientoService = procedimientoService;
		this.recursosService = recursosService;
		this.infoAdicionalService = infoAdicionalService;
		this.verificacionService = verificacionService;
		this.cotizacionService = cotizacionService;
		this.detalleService = detalleService;
		this.empleadoService = empleadoService;
	}

	// ================= LISTADO =================

	@GetMapping("/listar")
	public String listarPlanes(Model model) {
		model.addAttribute("planes", planService.listarPlanes());
		return "plan/listarplan";
	}

	// Cotizaciones APROBADAS que todavia pueden generar un plan de muestreo.
	@GetMapping("/pendientes")
	public String cotizacionesAprobadas(Model model) {
		List<CotizacionCResponseDto> aprobadas = new ArrayList<>();
		for (CotizacionCResponseDto c : cotizacionService.listarCotizaciones()) {
			if ("APROBADA".equalsIgnoreCase(c.getEstadoAprobacion())) {
				aprobadas.add(c);
			}
		}
		model.addAttribute("cotizaciones", aprobadas);
		return "plan/pendientesplan";
	}

	// ================= CREAR PLAN =================

	@GetMapping("/nuevo/{idCotizacion}")
	public String mostrarFormularioNuevo(@PathVariable int idCotizacion, Model model) {
		try {
			CotizacionCResponseDto cotizacion = cotizacionService.buscarPorId(idCotizacion);
			List<DetalleCResponseDto> detalles = detalleService.listarPorCotizacion(idCotizacion);

			PlanMuestreoPLRequestDto form = new PlanMuestreoPLRequestDto();
			form.setFechaElaboracion(new Date());

			model.addAttribute("plan", form);
			model.addAttribute("cotizacion", cotizacion);
			model.addAttribute("detalles", detalles);
			model.addAttribute("empleados", empleadoService.listarEmpleados());
			return "plan/nuevoplan";
		} catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/guardar")
	public String guardarPlan(@ModelAttribute PlanMuestreoPLRequestDto plan) {
		PlanMuestreoPLResponseDto creado = planService.guardar(plan);
		return "redirect:/plan/detalle/" + creado.getIdPlan() + "?success=true";
	}

	// ================= DETALLE (las 7 secciones) =================

	@GetMapping("/detalle/{idPlan}")
	public String verDetalle(@PathVariable int idPlan, Model model) {
		try {
			PlanMuestreoPLResponseDto plan = planService.buscarPorId(idPlan);

			model.addAttribute("plan", plan);
			model.addAttribute("matrices", matrizService.listarPorPlan(idPlan));
			model.addAttribute("parametros", parametroService.listarPorPlan(idPlan));
			model.addAttribute("tiposToma", tipoTomaService.listarPorPlan(idPlan));
			model.addAttribute("procedimientos", procedimientoService.listarPorPlan(idPlan));
			model.addAttribute("recursos", recursosService.listarPorPlan(idPlan));
			model.addAttribute("infoAdicional", infoAdicionalService.listarPorPlan(idPlan));
			model.addAttribute("verificaciones", verificacionService.listarPorPlan(idPlan));

			List<EmpleadoResponseDto> tecnicos = empleadoService.listarEmpleados();
			model.addAttribute("tecnicos", tecnicos);

			model.addAttribute("nuevaMatriz", new InformacionMatrizPLRequestDto());
			model.addAttribute("nuevoParametro", new ParametroAnalizarPLRequestDto());
			model.addAttribute("nuevoTipoToma", new TipoTomaFreHoraPLRequestDto());
			model.addAttribute("nuevoProcedimiento", new ProcedimientoMuePLRequestDto());
			model.addAttribute("nuevoRecurso", new RecursosCronoPLRequestDto());
			model.addAttribute("nuevaInfoAdicional", new InformacionAdicionalPLRequestDto());
			model.addAttribute("nuevaVerificacion", new VerificacionPLRequestDto());

			return "plan/detalleplan";
		} catch (Exception e) {
			return "error";
		}
	}

	@GetMapping("/eliminar/{idPlan}")
	public String eliminarPlan(@PathVariable int idPlan) {
		try {
			planService.eliminar(idPlan);
			return "redirect:/plan/listar?deleted=true";
		} catch (Exception e) {
			return "redirect:/plan/listar?error=true";
		}
	}

	// ================= SECCION 1: MATRIZ =================

	@PostMapping("/matriz/guardar/{idPlan}")
	public String guardarMatriz(@PathVariable int idPlan, @ModelAttribute InformacionMatrizPLRequestDto dto) {
		dto.setFkPlanMuestreo(idPlan);
		matrizService.guardar(dto);
		return "redirect:/plan/detalle/" + idPlan + "?success=true";
	}

	@GetMapping("/matriz/eliminar/{id}/{idPlan}")
	public String eliminarMatriz(@PathVariable int id, @PathVariable int idPlan) {
		matrizService.eliminar(id);
		return "redirect:/plan/detalle/" + idPlan + "?deleted=true";
	}

	// ================= SECCION 2: PARAMETROS =================

	@PostMapping("/parametro/guardar/{idPlan}")
	public String guardarParametro(@PathVariable int idPlan, @ModelAttribute ParametroAnalizarPLRequestDto dto) {
		dto.setFkPlanMuestreo(idPlan);
		parametroService.guardar(dto);
		return "redirect:/plan/detalle/" + idPlan + "?success=true";
	}

	@GetMapping("/parametro/eliminar/{id}/{idPlan}")
	public String eliminarParametro(@PathVariable int id, @PathVariable int idPlan) {
		parametroService.eliminar(id);
		return "redirect:/plan/detalle/" + idPlan + "?deleted=true";
	}

	// ================= SECCION 3: TIPO DE TOMA =================

	@PostMapping("/tipotoma/guardar/{idPlan}")
	public String guardarTipoToma(@PathVariable int idPlan, @ModelAttribute TipoTomaFreHoraPLRequestDto dto) {
		dto.setFkPlanMuestreo(idPlan);
		tipoTomaService.guardar(dto);
		return "redirect:/plan/detalle/" + idPlan + "?success=true";
	}

	@GetMapping("/tipotoma/eliminar/{id}/{idPlan}")
	public String eliminarTipoToma(@PathVariable int id, @PathVariable int idPlan) {
		tipoTomaService.eliminar(id);
		return "redirect:/plan/detalle/" + idPlan + "?deleted=true";
	}

	// ================= SECCION 4: PROCEDIMIENTO =================

	@PostMapping("/procedimiento/guardar/{idPlan}")
	public String guardarProcedimiento(@PathVariable int idPlan, @ModelAttribute ProcedimientoMuePLRequestDto dto) {
		dto.setFkPlanMuestreo(idPlan);
		procedimientoService.guardar(dto);
		return "redirect:/plan/detalle/" + idPlan + "?success=true";
	}

	@GetMapping("/procedimiento/eliminar/{id}/{idPlan}")
	public String eliminarProcedimiento(@PathVariable int id, @PathVariable int idPlan) {
		procedimientoService.eliminar(id);
		return "redirect:/plan/detalle/" + idPlan + "?deleted=true";
	}

	// ================= SECCION 5: RECURSOS Y CRONOGRAMA =================

	@PostMapping("/recurso/guardar/{idPlan}")
	public String guardarRecurso(@PathVariable int idPlan, @ModelAttribute RecursosCronoPLRequestDto dto) {
		dto.setFkPlanMuestreo(idPlan);
		recursosService.guardar(dto);
		return "redirect:/plan/detalle/" + idPlan + "?success=true";
	}

	@GetMapping("/recurso/eliminar/{id}/{idPlan}")
	public String eliminarRecurso(@PathVariable int id, @PathVariable int idPlan) {
		recursosService.eliminar(id);
		return "redirect:/plan/detalle/" + idPlan + "?deleted=true";
	}

	// ================= SECCION 6: INFORMACION ADICIONAL =================

	@PostMapping("/infoadicional/guardar/{idPlan}")
	public String guardarInfoAdicional(@PathVariable int idPlan, @ModelAttribute InformacionAdicionalPLRequestDto dto) {
		dto.setFkPlanMuestreo(idPlan);
		infoAdicionalService.guardar(dto);
		return "redirect:/plan/detalle/" + idPlan + "?success=true";
	}

	@GetMapping("/infoadicional/eliminar/{id}/{idPlan}")
	public String eliminarInfoAdicional(@PathVariable int id, @PathVariable int idPlan) {
		infoAdicionalService.eliminar(id);
		return "redirect:/plan/detalle/" + idPlan + "?deleted=true";
	}

	// ================= SECCION 7: VERIFICACION =================

	@PostMapping("/verificacion/guardar/{idPlan}")
	public String guardarVerificacion(@PathVariable int idPlan, @ModelAttribute VerificacionPLRequestDto dto) {
		dto.setFkPlanMuestreo(idPlan);
		verificacionService.guardar(dto);
		return "redirect:/plan/detalle/" + idPlan + "?success=true";
	}

	@GetMapping("/verificacion/eliminar/{id}/{idPlan}")
	public String eliminarVerificacion(@PathVariable int id, @PathVariable int idPlan) {
		verificacionService.eliminar(id);
		return "redirect:/plan/detalle/" + idPlan + "?deleted=true";
	}

}
