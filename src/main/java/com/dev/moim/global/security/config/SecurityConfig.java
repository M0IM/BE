package com.dev.moim.global.security.config;

import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.global.redis.service.LogoutAccessTokenService;
import com.dev.moim.global.redis.service.RefreshTokenService;
import com.dev.moim.global.config.CorsConfig;
import com.dev.moim.global.security.exception.JwtAccessDeniedHandler;
import com.dev.moim.global.security.exception.JwtAuthenticationEntryPoint;
import com.dev.moim.global.security.filter.*;
import com.dev.moim.global.security.principal.PrincipalDetailsService;
import com.dev.moim.global.security.service.OAuthLoginService;
import com.dev.moim.global.security.util.HttpResponseUtil;
import com.dev.moim.global.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.Map;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalDetailsService principalDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenService refreshTokenService;
    private final Map<Provider, OAuthLoginService> oAuthLoginServiceMap;

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
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwtUtil, LogoutAccessTokenService logoutAccessTokenService) throws Exception {
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

        CustomLoginFilter customLoginFilter = new CustomLoginFilter(
                authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenService
        );
        customLoginFilter.setFilterProcessesUrl("/api/v1/auth/login");

        OAuthLoginFilter oAuthLoginFilter = new OAuthLoginFilter(
                oAuthLoginServiceMap, jwtUtil, refreshTokenService
        );

        http.addFilterAt(customLoginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(oAuthLoginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtFilter(jwtUtil, principalDetailsService, logoutAccessTokenService), CustomLoginFilter.class);        http.addFilterBefore(new JwtExceptionFilter(), JwtFilter.class);
        http.addFilterBefore(new JwtExceptionFilter(), JwtFilter.class);

        http.logout(logout -> logout
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(
                        new CustomLogoutHandler(logoutAccessTokenService, refreshTokenService, jwtUtil))
                .logoutSuccessHandler((request, response, authentication) -> HttpResponseUtil.setSuccessResponse(
                        response,
                        HttpStatus.OK,
                        "로그아웃 성공")
                )
        );
        http.addFilterAfter(
                new LogoutFilter(
                        (request, response, authentication) -> HttpResponseUtil.setSuccessResponse(response, HttpStatus.OK, "로그아웃 성공"),
                        new CustomLogoutHandler(logoutAccessTokenService, refreshTokenService, jwtUtil)),
                JwtFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
