package com.dev.moim.global.security.filter;

import com.dev.moim.domain.account.dto.OAuthLoginRequest;
import com.dev.moim.domain.account.dto.TokenResponse;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.redis.service.RefreshTokenService;
import com.dev.moim.global.security.feign.dto.OAuthUserInfo;
import com.dev.moim.global.security.principal.PrincipalDetails;
import com.dev.moim.global.security.service.OAuthLoginService;
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
import java.util.Map;

import static com.dev.moim.global.common.code.status.ErrorStatus._BAD_REQUEST;

@Slf4j
public class OAuthLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final Map<Provider, OAuthLoginService> oAuthLoginServiceMap;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public OAuthLoginFilter(Map<Provider, OAuthLoginService> oAuthLoginServiceMap, JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        super(new AntPathRequestMatcher("/api/v1/auth/oauth"));
        this.oAuthLoginServiceMap = oAuthLoginServiceMap;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public Authentication attemptAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response) throws IOException, ServletException {

        log.info("** OAuthLoginFilter **");

        OAuthLoginRequest oAuthLoginRequest = readBody(request);

        log.info("oAuthToken = {}", oAuthLoginRequest.oAuthToken());
        log.info("provider = {}", oAuthLoginRequest.provider());

        OAuthLoginService oAuthLoginService = oAuthLoginServiceMap.get(oAuthLoginRequest.provider());

        OAuthUserInfo oAuthUserInfo = oAuthLoginService.getUserInfo(oAuthLoginRequest.oAuthToken());

        User user = oAuthLoginService.findOrCreateUser(oAuthUserInfo);

        PrincipalDetails principalDetails = new PrincipalDetails(user);

        String accessToken = jwtUtil.createAccessToken(principalDetails);
        String refreshToken = jwtUtil.createRefreshToken(principalDetails);

        refreshTokenService.saveToken(principalDetails.user().getEmail(), refreshToken, jwtUtil.getRefreshTokenValiditySec());

        HttpResponseUtil.setSuccessResponse(response, HttpStatus.CREATED, new TokenResponse(accessToken, refreshToken));

        return null;
    }

    private OAuthLoginRequest readBody(HttpServletRequest request) {
        OAuthLoginRequest requestDTO = null;
        ObjectMapper om = new ObjectMapper();

        try {
            requestDTO = om.readValue(request.getInputStream(), OAuthLoginRequest.class);
        } catch (IOException e) {
            throw new AuthException(_BAD_REQUEST);
        }

        return requestDTO;
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
        HttpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED, "Authentication failed");
    }
}
