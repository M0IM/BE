package com.dev.moim.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Bean
    public static CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        List<String> allowedOriginPatterns = Arrays.asList("*");
        configuration.setAllowedOriginPatterns(allowedOriginPatterns);

        List<String> allowedHttpMethods = Arrays.asList("GET", "POST", "PUT", "DELETE");
        configuration.setAllowedMethods(allowedHttpMethods);

        List<String> allowedHeaders = Arrays.asList("*");
        configuration.setAllowedHeaders(allowedHeaders);

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
