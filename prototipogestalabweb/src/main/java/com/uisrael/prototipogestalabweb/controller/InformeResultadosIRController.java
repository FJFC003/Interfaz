package com.uisrael.prototipogestalabweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/proyeccionInformeResultados")
public class InformeResultadosIRController {
	
@GetMapping
	
	public String leerPagina (){
		return "/proyeccionInformeResultados/informeresultados";
		
		
	}

}
