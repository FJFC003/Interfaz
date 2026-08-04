package com.uisrael.prototipogestalabweb.configuration;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Evita que el navegador guarde en cache las paginas protegidas.
 *
 * Sin estas cabeceras, al cerrar sesion y pulsar el boton "Atras" el navegador
 * muestra el HTML que tenia guardado sin volver a pedirlo al servidor. La sesion
 * ya esta invalidada, pero los datos siguen visibles en pantalla.
 *
 * "no-store" es la directiva que realmente lo impide: es la unica que desactiva
 * el cache de historial (bfcache) en Chrome y Firefox.
 */
public class CabecerasNoCacheInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		return true;
	}

}
