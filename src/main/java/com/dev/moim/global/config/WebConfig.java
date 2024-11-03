package com.dev.moim.global.config;

import com.dev.moim.global.security.annotation.resolver.AuthUserArgumentResolver;
import com.dev.moim.global.security.annotation.resolver.AuthUserMoimAdminArgumentResolver;
import com.dev.moim.global.security.annotation.resolver.AuthUserMoimArgumentResolver;
import com.dev.moim.global.security.annotation.resolver.ExtractTokenArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthUserArgumentResolver authUserArgumentResolver;
    private final ExtractTokenArgumentResolver extractTokenArgumentResolver;
    private final AuthUserMoimArgumentResolver authUserMoimArgumentResolver;
    private final AuthUserMoimAdminArgumentResolver authUserMoimAdminArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserArgumentResolver);
        resolvers.add(extractTokenArgumentResolver);
        resolvers.add(authUserMoimArgumentResolver);
        resolvers.add(authUserMoimAdminArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(6000);
    }
}
