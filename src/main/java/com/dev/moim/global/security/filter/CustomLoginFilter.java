package com.dev.moim.global.security.filter;

import com.dev.moim.domain.account.dto.LoginRequest;
import com.dev.moim.domain.account.dto.TokenResponse;
import com.dev.moim.global.redis.util.RedisUtil;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.security.principal.PrincipalDetails;
import com.dev.moim.global.security.util.HttpRequestUtil;
import com.dev.moim.global.security.util.HttpResponseUtil;
import com.dev.moim.global.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@RequiredArgsConstructor
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Override
    public Authentication attemptAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response) throws AuthenticationException {
        log.info("** LoginFilter **");

        LoginRequest logInRequest = HttpRequestUtil.readBody(request, LoginRequest.class);

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

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String accessToken = jwtUtil.createAccessToken(principalDetails);
        String refreshToken = jwtUtil.createRefreshToken(principalDetails);

        redisUtil.setValue(principalDetails.user().getEmail(), refreshToken, jwtUtil.getRefreshTokenValiditySec());

        HttpResponseUtil.setSuccessResponse(response, HttpStatus.CREATED, new TokenResponse(accessToken, refreshToken));
    }

    @Override
    protected void unsuccessfulAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull AuthenticationException failed) throws IOException {
        ErrorStatus errorStatus;

        if (failed instanceof UsernameNotFoundException) {
            errorStatus = USER_NOT_FOUND;
        } else if (failed instanceof BadCredentialsException) {
            errorStatus = BAD_CREDENTIALS;
        } else {
            errorStatus = AUTHENTICATION_FAILED;
        }
        BaseResponse<Object> errorResponse = BaseResponse.onFailure(
                errorStatus.getCode(),
                errorStatus.getMessage(),
                null);

        HttpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);
    }
}