package com.ato.backendapi.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // Autoriser toutes les origines
        corsConfiguration.addAllowedHeader("*"); // Autoriser tous les headers
        corsConfiguration.addAllowedMethod("*"); // Autoriser toutes les méthodes HTTP (GET, POST, PUT, DELETE, etc.)
        corsConfiguration.setAllowCredentials(true); // Si vous avez des credentials (comme des cookies ou des headers d'autorisation)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Appliquer cette configuration à toutes les routes

        return new CorsFilter(source);
    }
}

