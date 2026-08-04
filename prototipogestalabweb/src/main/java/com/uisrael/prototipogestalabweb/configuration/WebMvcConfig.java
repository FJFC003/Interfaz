package com.uisrael.prototipogestalabweb.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// Primero las cabeceras de no-cache: se aplican a las vistas, nunca a los
		// recursos estaticos (si no, el navegador volveria a descargar jQuery,
		// DataTables y FontAwesome en cada pagina).
		registry.addInterceptor(new CabecerasNoCacheInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns(
						"/css/**",
						"/js/**",
						"/vendor/**",
						"/img/**",
						"/scss/**"
				);

		registry.addInterceptor(new SesionInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns(
						"/login",
						"/logout",
						// La recuperacion de acceso es publica por definicion:
						// quien la usa es precisamente quien no puede iniciar sesion.
						"/recuperar",
						"/recuperar/**",
						"/css/**",
						"/js/**",
						"/vendor/**",
						"/img/**",
						"/scss/**"
				);
	}

}