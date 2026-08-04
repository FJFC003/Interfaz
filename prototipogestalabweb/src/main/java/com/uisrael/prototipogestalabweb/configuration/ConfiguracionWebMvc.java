package com.uisrael.prototipogestalabweb.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfiguracionWebMvc implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SeguridadInterceptor())
				// Solo las rutas de los modulos protegidos llegan al interceptor.
				.addPathPatterns("/empleado/**", "/area/**", "/cargo/**", "/rol/**")
				// El login, la recuperacion y los recursos estaticos quedan fuera.
				.excludePathPatterns("/login", "/logout", "/recuperar", "/recuperar/**",
						"/css/**", "/js/**", "/img/**", "/vendor/**");
	}

}