package com.uisrael.prototipogestalabweb.configuration;

import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.HandlerInterceptor;

import com.uisrael.prototipogestalabweb.model.dto.response.LoginResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Control de acceso por ruta.
 *
 * Solo se listan los prefijos que necesitan proteccion; cualquier ruta que no
 * aparezca en el mapa pasa sin restriccion. Esto es intencional: el objetivo es
 * cerrar los modulos sensibles sin alterar el resto del sistema.
 */
public class SeguridadInterceptor implements HandlerInterceptor {

	/** Prefijo de ruta -> roles que pueden entrar. */
	private static final Map<String, List<String>> RUTAS_PROTEGIDAS = Map.of(
			"/empleado", List.of("Gerente General"),
			"/area",     List.of("Gerente General"),
			"/cargo",    List.of("Gerente General"),
			"/rol",      List.of("Gerente General"));

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String ruta = request.getRequestURI();

		List<String> rolesPermitidos = null;
		for (Map.Entry<String, List<String>> entrada : RUTAS_PROTEGIDAS.entrySet()) {
			if (ruta.startsWith(entrada.getKey())) {
				rolesPermitidos = entrada.getValue();
				break;
			}
		}

		// Ruta no protegida: sigue su curso normal.
		if (rolesPermitidos == null) {
			return true;
		}

		HttpSession session = request.getSession(false);
		Object usuario = session != null ? session.getAttribute("usuarioActual") : null;

		if (!(usuario instanceof LoginResponseDto sesion)) {
			response.sendRedirect("/login");
			return false;
		}

		String rol = sesion.getRol() == null ? "" : sesion.getRol().trim();
		boolean autorizado = rolesPermitidos.stream().anyMatch(permitido -> permitido.equalsIgnoreCase(rol));

		if (!autorizado) {
			response.sendRedirect("/?sinPermiso=true");
			return false;
		}

		return true;
	}

}
