package com.dev.moim.global.security.filter;

import com.dev.moim.domain.account.dto.OAuthLoginRequest;
import com.dev.moim.domain.account.dto.OIDCDecodePayload;
import com.dev.moim.domain.account.dto.TokenResponse;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.common.code.status.SuccessStatus;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.redis.util.RedisUtil;
import com.dev.moim.global.security.principal.PrincipalDetails;
import com.dev.moim.global.security.service.OIDCService;
import com.dev.moim.global.security.util.HttpRequestUtil;
import com.dev.moim.global.security.util.HttpResponseUtil;
import com.dev.moim.global.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class OAuthLoginFilter extends OncePerRequestFilter {

    private final OIDCService oidcService;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.info("** OAuthLoginFilter **");

        if (!request.getServletPath().equals("/api/v1/auth/oAuth")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            authenticate(request, response);
        } catch (AuthException e) {
            HttpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public void authenticate (
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException, AuthException {

        OAuthLoginRequest oAuthLoginRequest = HttpRequestUtil.readBody(request, OAuthLoginRequest.class);

        log.info("provider : {}", oAuthLoginRequest.provider());
        log.info("idToken : {}", oAuthLoginRequest.idToken());

        OIDCDecodePayload oidcDecodePayload = oidcService.getOIDCDecodePayload(oAuthLoginRequest.provider(), oAuthLoginRequest.idToken());

        log.info("[payload] email : {}", oidcDecodePayload.email());
        log.info("[payload] sub (사용자 고유 식별자 키) : {}", oidcDecodePayload.sub());

        Optional<User> user = userRepository.findByProviderIdAndProvider(oidcDecodePayload.sub(), oAuthLoginRequest.provider());

        if (user.isPresent()) {
            handleExistingUser(response, user.get());
        } else {
            log.info("신규 유저 : 추가 정보 입력 필요");
            HttpResponseUtil.setSuccessResponse(response, SuccessStatus.UNREGISTERED_OAUTH_LOGIN_USER.getHttpStatus(),
                    SuccessStatus.UNREGISTERED_OAUTH_LOGIN_USER.getMessage());
        }
    }

    private void handleExistingUser(HttpServletResponse response, User existingUser) throws IOException {
        PrincipalDetails principalDetails = new PrincipalDetails(existingUser);

        String accessToken = jwtUtil.createAccessToken(principalDetails);
        String refreshToken = jwtUtil.createRefreshToken(principalDetails);

        redisUtil.setValue(principalDetails.user().getEmail(), refreshToken, jwtUtil.getRefreshTokenValiditySec());

        HttpResponseUtil.setSuccessResponse(response, HttpStatus.OK, new TokenResponse(accessToken, refreshToken));
    }
}
