package com.dev.moim.global.security.filter;

import com.dev.moim.domain.account.dto.OAuthLoginRequest;
import com.dev.moim.domain.account.dto.OIDCDecodePayload;
import com.dev.moim.domain.account.dto.TokenResponse;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.redis.util.RedisUtil;
import com.dev.moim.global.security.principal.PrincipalDetails;
import com.dev.moim.global.security.service.OIDCService;
import com.dev.moim.global.security.util.HttpResponseUtil;
import com.dev.moim.global.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

import static com.dev.moim.global.common.code.status.ErrorStatus.UNREGISTERED_OAUTH_LOGIN_USER;
import static com.dev.moim.global.common.code.status.ErrorStatus._BAD_REQUEST;

@Slf4j
public class OAuthLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final OIDCService oidcService;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    public OAuthLoginFilter(OIDCService oidcService, JwtUtil jwtUtil, RedisUtil redisUtil, UserRepository userRepository) {
        super(new AntPathRequestMatcher("/api/v1/auth/oAuth"));
        this.oidcService = oidcService;
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response) throws IOException, ServletException {

        log.info("** OAuthLoginFilter **");

        OAuthLoginRequest oAuthLoginRequest = readBody(request);

        log.info("provider = {}", oAuthLoginRequest.provider());
        log.info("idToken = {}", oAuthLoginRequest.idToken());

        OIDCDecodePayload oidcDecodePayload = oidcService.getOIDCDecodePayload(oAuthLoginRequest.provider(), oAuthLoginRequest.idToken());

        log.info("oidcDecodePayload.email() = {}", oidcDecodePayload.email());
        log.info("oidcDecodePayload.sub() = {}", oidcDecodePayload.sub());

        User user = userRepository.findByProviderIdAndProvider(oidcDecodePayload.sub(), oAuthLoginRequest.provider())
                .orElseThrow(() -> new AuthException(UNREGISTERED_OAUTH_LOGIN_USER));

        PrincipalDetails principalDetails = new PrincipalDetails(user);

        String accessToken = jwtUtil.createAccessToken(principalDetails);
        String refreshToken = jwtUtil.createRefreshToken(principalDetails);

        redisUtil.setValue(principalDetails.user().getEmail(), refreshToken, jwtUtil.getRefreshTokenValiditySec());

        HttpResponseUtil.setSuccessResponse(response, HttpStatus.CREATED, new TokenResponse(accessToken, refreshToken));

        return null;
    }

    private OAuthLoginRequest readBody(HttpServletRequest request) {
        OAuthLoginRequest oAuthLoginRequest = null;
        ObjectMapper om = new ObjectMapper();

        try {
            oAuthLoginRequest = om.readValue(request.getInputStream(), OAuthLoginRequest.class);
        } catch (IOException e) {
            throw new AuthException(_BAD_REQUEST);
        }

        return oAuthLoginRequest;
    }

    @Override
    protected void successfulAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain,
            @NonNull Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        if (failed.getCause() instanceof AuthException) {
            AuthException authException = (AuthException) failed.getCause();
            if (authException.getCode() == UNREGISTERED_OAUTH_LOGIN_USER) {
                HttpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED, authException.getMessage());
                return;
            }
        }
        HttpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED, "Authentication failed");
    }
}
