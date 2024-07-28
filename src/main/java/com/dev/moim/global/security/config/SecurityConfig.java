package com.dev.moim.global.security.config;

import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.redis.util.RedisUtil;
import com.dev.moim.global.config.CorsConfig;
import com.dev.moim.global.security.exception.JwtAccessDeniedHandler;
import com.dev.moim.global.security.exception.JwtAuthenticationEntryPoint;
import com.dev.moim.global.security.filter.*;
import com.dev.moim.global.security.principal.PrincipalDetailsService;
import com.dev.moim.global.security.service.OIDCService;
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

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalDetailsService principalDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final OIDCService oidcService;

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
                .requestMatchers(allowUrls).permitAll()
                .requestMatchers("/**").authenticated()
                .anyRequest().permitAll());

        CustomLoginFilter customLoginFilter = new CustomLoginFilter(
                authenticationManager(authenticationConfiguration), jwtUtil, redisUtil
        );
        customLoginFilter.setFilterProcessesUrl("/api/v1/auth/login");

        OAuthLoginFilter OAuthLoginFilter = new OAuthLoginFilter(
                oidcService, jwtUtil, redisUtil, userRepository
        );

        http.addFilterAt(customLoginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(OAuthLoginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtFilter(jwtUtil, principalDetailsService, redisUtil), CustomLoginFilter.class);
        http.addFilterBefore(new JwtExceptionFilter(), JwtFilter.class);

        http.logout(logout -> logout
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(
                        new CustomLogoutHandler(jwtUtil, redisUtil))
                .logoutSuccessHandler((request, response, authentication) -> HttpResponseUtil.setSuccessResponse(
                        response,
                        HttpStatus.OK,
                        "로그아웃 성공")
                )
        );
        http.addFilterAfter(
                new LogoutFilter(
                        (request, response, authentication) -> HttpResponseUtil.setSuccessResponse(response, HttpStatus.OK, "로그아웃 성공"),
                        new CustomLogoutHandler(jwtUtil, redisUtil)),
                JwtFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
