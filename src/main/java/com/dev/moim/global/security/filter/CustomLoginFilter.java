package com.dev.moim.global.security.filter;

import com.dev.moim.domain.account.dto.LoginRequest;
import com.dev.moim.domain.account.dto.TokenResponse;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.firebase.service.FcmQueryService;
import com.dev.moim.global.redis.util.RedisUtil;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.security.event.CustomAuthenticationSuccessEvent;
import com.dev.moim.global.security.principal.PrincipalDetails;
import com.dev.moim.global.util.HttpRequestUtil;
import com.dev.moim.global.util.HttpResponseUtil;
import com.dev.moim.global.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;
import static com.dev.moim.global.common.code.status.SuccessStatus._OK;

@Slf4j
@RequiredArgsConstructor
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final ApplicationEventPublisher eventPublisher;
    private final FcmQueryService fcmQueryService;

    @Override
    public Authentication attemptAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response) throws AuthenticationException {

        LoginRequest logInRequest = HttpRequestUtil.readBody(request, LoginRequest.class);
        String fcmToken = Optional.ofNullable(logInRequest.fcmToken())
                .filter(token -> !token.trim().isEmpty())
                .orElseThrow(() -> new AuthException(FCM_TOKEN_REQUIRED));
        // fcmQueryService.isTokenValid("MOIM", fcmToken);
        request.setAttribute("fcmToken", fcmToken);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                UsernamePasswordAuthenticationToken.unauthenticated(logInRequest.email(), logInRequest.password());

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain,
            @NonNull Authentication authResult) throws IOException{
        SecurityContextHolder.getContext().setAuthentication(authResult);

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String accessToken = jwtUtil.createAccessToken(principalDetails);
        String refreshToken = jwtUtil.createRefreshToken(principalDetails);

        try {
            redisUtil.setValue(principalDetails.user().getId().toString(), refreshToken, jwtUtil.getRefreshTokenValiditySec());
        } catch (RedisConnectionFailureException e) {
            throw new AuthException(REDIS_CONNECTION_ERROR);
        }

        eventPublisher.publishEvent(new CustomAuthenticationSuccessEvent(principalDetails, request.getAttribute("fcmToken").toString()));

        HttpResponseUtil.setSuccessResponse(response, _OK, new TokenResponse(accessToken, refreshToken, principalDetails.getProvider()));
    }

    @Override
    protected void unsuccessfulAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull AuthenticationException failed) throws IOException {
        ErrorStatus errorStatus;

        if (failed instanceof UsernameNotFoundException) {
            errorStatus = USER_UNREGISTERED;
        } else if (failed instanceof BadCredentialsException) {
            errorStatus = BAD_CREDENTIALS;
        } else {
            errorStatus = AUTHENTICATION_FAILED;
        }
        log.error("[ERROR] : {}", errorStatus);

        BaseResponse<Object> errorResponse = BaseResponse.onFailure(
                errorStatus.getCode(),
                errorStatus.getMessage(),
                null);

        HttpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);
    }
}