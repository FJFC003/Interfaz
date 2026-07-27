package com.uisrael.prototipogestalabweb.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
	
	@Value("${gestalab.api.base-url}")
    private String baseUrl;
	
	@Bean
	WebClient webClient(WebClient.Builder builder) {
		return builder.baseUrl("http://localhost:8080/api")
				.build();
	}

}
