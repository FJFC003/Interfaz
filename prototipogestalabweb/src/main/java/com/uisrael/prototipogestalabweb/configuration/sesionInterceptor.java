package com.uisrael.prototipogestalabweb.configuration;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class sesionInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(false);
		boolean autenticado = session != null && session.getAttribute("usuarioActual") != null;

		if (!autenticado) {
			response.sendRedirect("/login");
			return false;
		}
		return true;
	}

}