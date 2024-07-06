package com.dev.moim.global.security.filter;

import com.dev.moim.domain.account.dto.LogInRequest;
import com.dev.moim.domain.account.dto.TokenResponse;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.security.principal.PrincipalDetails;
import com.dev.moim.global.security.provider.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public Authentication attemptAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response) throws AuthenticationException {
        LogInRequest logInRequest = readBody(request);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                UsernamePasswordAuthenticationToken.unauthenticated(logInRequest.email(), logInRequest.password());

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    private LogInRequest readBody(HttpServletRequest request) {
        LogInRequest loginRequestDto = null;
        ObjectMapper om = new ObjectMapper();

        try {
            loginRequestDto = om.readValue(request.getInputStream(), LogInRequest.class);
        } catch (IOException e) {
            throw new AuthException(_BAD_REQUEST);
        }

        return loginRequestDto;
    }

    @Override
    protected void successfulAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain,
            @NonNull Authentication authResult) throws IOException{

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

//        String token = jwtProvider.createAccessToken(principalDetails);
//
//        response.addHeader("Authorization", "Bearer " + token);
//
//        response.setContentType("application/json; charset=UTF-8");
//        response.setStatus(HttpStatus.OK.value());
//

        TokenResponse tokenResponse = new TokenResponse(
                jwtProvider.createAccessToken(principalDetails),
                jwtProvider.createRefreshToken(principalDetails));

        BaseResponse<Object> errorResponse = BaseResponse.onSuccess(tokenResponse);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
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
        throw new AuthException(errorStatus);
    }
}