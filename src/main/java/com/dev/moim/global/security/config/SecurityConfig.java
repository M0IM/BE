package com.dev.moim.global.security.config;

import com.dev.moim.global.redis.service.RefreshTokenService;
import com.dev.moim.global.config.CorsConfig;
import com.dev.moim.global.security.exception.JwtAccessDeniedHandler;
import com.dev.moim.global.security.exception.JwtAuthenticationEntryPoint;
import com.dev.moim.global.security.filter.JwtExceptionFilter;
import com.dev.moim.global.security.filter.JwtFilter;
import com.dev.moim.global.security.filter.LoginFilter;
import com.dev.moim.global.security.principal.PrincipalDetailsService;
import com.dev.moim.global.security.util.JwtUtil;
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
    private final RefreshTokenService refreshTokenService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    private final String[] allowUrls = {
            "/swagger-ui/**",
            "/v3/**",
            "/api-docs/**",
            "/api/v1/auth/join/**",
            "/api/v1/auth/reissueToken/**",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors
                .configurationSource(CorsConfig.apiConfigurationSource()));

        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy((SessionCreationPolicy.STATELESS)));

        http.exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler));

        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(allowUrls).permitAll()
                .requestMatchers("/**").authenticated()
                .anyRequest().permitAll());

        LoginFilter loginFilter = new LoginFilter(
                authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenService
        );
        loginFilter.setFilterProcessesUrl("/api/v1/auth/login");

        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtFilter(jwtUtil, principalDetailsService), LoginFilter.class);
        http.addFilterBefore(new JwtExceptionFilter(), JwtFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
