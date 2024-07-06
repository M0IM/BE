package com.dev.moim.global.security.config;

import com.dev.moim.global.config.CorsConfig;
import com.dev.moim.global.security.exception.JwtAccessDeniedHandler;
import com.dev.moim.global.security.exception.JwtAuthenticationEntryPoint;
import com.dev.moim.global.security.filter.JwtExceptionFilter;
import com.dev.moim.global.security.filter.JwtFilter;
import com.dev.moim.global.security.filter.LoginFilter;
import com.dev.moim.global.security.principal.PrincipalDetailsService;
import com.dev.moim.global.security.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalDetailsService principalDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    private final String[] allowUrls = {
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/swagger",
            "/swagger-ui.html",
            "/api-docs", "/api-docs/**",
            "/api/v1/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtProvider jwtProvider) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors
                .configurationSource(CorsConfig.apiConfigurationSource()));

        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.headers(
                headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy((SessionCreationPolicy.STATELESS)));

        http.exceptionHandling(
                configurer -> configurer
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler));

        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(allowUrls).permitAll()
                .anyRequest().authenticated());

        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtProvider),
                UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtFilter(jwtProvider, principalDetailsService), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtExceptionFilter(), JwtFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
