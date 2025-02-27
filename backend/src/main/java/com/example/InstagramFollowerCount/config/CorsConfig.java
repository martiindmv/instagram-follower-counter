package com.example.InstagramFollowerCount.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow all origins during development (restrict this in production)
        config.addAllowedOrigin("http://localhost:3000"); // React dev server
        config.addAllowedOrigin("http://localhost:8080"); // Spring Boot server
        // Allow common HTTP methods
        config.addAllowedMethod("*"); // Allow all methods for simplicity during development
        // Allow all headers
        config.addAllowedHeader("*");
        // Allow credentials (if needed)
        config.setAllowCredentials(true);
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}