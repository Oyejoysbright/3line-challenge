package org.jpc.super_service.configs;

import java.util.Arrays;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {
    
        @Autowired
        private Properties properties;
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOriginPatterns(Collections.singletonList(properties.getAllowedOrigin()));
		configuration.setAllowedMethods(Arrays.asList(new String[] {"POST"}));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Arrays.asList(
				new String[] { "Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin" }));

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

}
