package com.dev.moim.global.security.config;

import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.user.service.UserCommandService;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.common.code.status.SuccessStatus;
import com.dev.moim.global.firebase.service.FcmQueryService;
import com.dev.moim.global.redis.util.RedisUtil;
import com.dev.moim.global.config.CorsConfig;
import com.dev.moim.global.security.exception.JwtAccessDeniedHandler;
import com.dev.moim.global.security.exception.JwtAuthenticationEntryPoint;
import com.dev.moim.global.security.filter.*;
import com.dev.moim.global.security.principal.PrincipalDetailsService;
import com.dev.moim.global.security.service.NaverLoginService;
import com.dev.moim.global.security.service.OIDCService;
import com.dev.moim.global.util.HttpResponseUtil;
import com.dev.moim.global.security.util.JwtUtil;
import com.dev.moim.global.security.util.NaverLoginAuthenticationProvider;
import com.dev.moim.global.security.util.OIDCAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.Arrays;

@Configuration
// @EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final OIDCService oidcService;
    private final NaverLoginService naverLoginService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final PrincipalDetailsService principalDetailsService;
    private final ApplicationEventPublisher eventPublisher;
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final FcmQueryService fcmQueryService;
    private final Environment environment;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {

        ProviderManager authenticationManager = null;
        try {
            authenticationManager = (ProviderManager) authenticationConfiguration.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        authenticationManager.getProviders().add(daoAuthenticationProvider());
        authenticationManager.getProviders().add(oidcAuthenticationProvider());
        authenticationManager.getProviders().add(naverLoginAuthenticationProvider());

        return authenticationConfiguration.getAuthenticationManager();
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(principalDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    public OIDCAuthenticationProvider oidcAuthenticationProvider() {
        return new OIDCAuthenticationProvider(oidcService);
    }

    public NaverLoginAuthenticationProvider naverLoginAuthenticationProvider() {
        return new NaverLoginAuthenticationProvider(naverLoginService);
    }

    private static final String[] allowUrls = {
            "/swagger-ui/**",
            "/v3/**",
            "/api-docs/**",
            "/api/v1/auth/join/**",
            "/api/v1/auth/emails/**",
            "/api/v1/auth/reissueToken/**",
            "/health",
            "/api/v1/auth/password/**",
            "/api/v1/regions/**"
    };

    private static final String[] releaseAllowUrls = {
            "/api/v1/auth/join/**",
            "/api/v1/auth/emails/**",
            "/api/v1/auth/reissueToken/**",
            "/health",
            "/api/v1/auth/password/**",
            "/api/v1/regions/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwtUtil, RedisUtil redisUtil, UserRepository userRepository) throws Exception {

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
                .requestMatchers(Arrays.asList(environment.getActiveProfiles()).contains("release") ? releaseAllowUrls : allowUrls).permitAll()
                .requestMatchers("/**").authenticated()
                .anyRequest().permitAll());

        CustomLoginFilter customLoginFilter = new CustomLoginFilter(
                authenticationManager(), jwtUtil, redisUtil, eventPublisher, fcmQueryService);
        customLoginFilter.setFilterProcessesUrl("/api/v1/auth/login");

        OAuthLoginFilter oAuthLoginFilter = new OAuthLoginFilter(
                jwtUtil, redisUtil, authenticationManager(), userRepository, eventPublisher, fcmQueryService);

        http.addFilterAt(customLoginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(oAuthLoginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtFilter(jwtUtil,redisUtil, Arrays.asList(environment.getActiveProfiles()).contains("release") ? releaseAllowUrls : allowUrls), CustomLoginFilter.class);
        http.addFilterBefore(new JwtExceptionFilter(Arrays.asList(environment.getActiveProfiles()).contains("release") ? releaseAllowUrls : allowUrls), JwtFilter.class);

        http.logout(logout -> logout
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(
                        new CustomLogoutHandler(jwtUtil, redisUtil, userCommandService, userQueryService))
                .logoutSuccessHandler((request, response, authentication) -> HttpResponseUtil.setSuccessResponse(
                        response,
                        SuccessStatus._OK,
                        "로그아웃 성공")
                )
        );
        http.addFilterAfter(
                new LogoutFilter(
                        (request, response, authentication) -> HttpResponseUtil.setSuccessResponse(response, SuccessStatus._OK, "로그아웃 성공"),
                        new CustomLogoutHandler(jwtUtil, redisUtil, userCommandService, userQueryService)),
                JwtFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
