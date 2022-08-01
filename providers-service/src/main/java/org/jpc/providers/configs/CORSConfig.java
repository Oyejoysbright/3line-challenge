package org.jpc.providers.configs;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOriginPatterns(Collections.singletonList("*")); // set access from all domains
		configuration.setAllowedMethods(Arrays.asList(new String[] { "GET", "POST", "PUT", "DELETE" }));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Arrays.asList(
				new String[] { "Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin" }));

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

}
