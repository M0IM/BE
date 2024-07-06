package com.dev.moim.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

import static io.swagger.v3.oas.models.security.SecurityScheme.*;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI moimAPI() {
        Info info = new Info().title("Moim API").description("Moim API 명세").version("1.0.0");

        String jwtSchemeName = "JWT TOKEN";
        SecurityScheme securityScheme = new SecurityScheme()
                .type(Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");
        Components components =
                new Components().addSecuritySchemes("bearerAuth",securityScheme);

        return new OpenAPI()
                .security(Arrays.asList(securityRequirement))
                .components(components)
                .info(info);
    }
}
