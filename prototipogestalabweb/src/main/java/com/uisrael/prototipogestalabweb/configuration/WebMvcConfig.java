package com.uisrael.prototipogestalabweb.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
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