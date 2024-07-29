package com.dev.moim.global.security.filter;

import com.dev.moim.domain.account.dto.OAuthLoginRequest;
import com.dev.moim.domain.account.dto.OIDCDecodePayload;
import com.dev.moim.domain.account.dto.TokenResponse;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.repository.UserRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.dev.moim.global.common.code.status.SuccessStatus.UNREGISTERED_OAUTH_LOGIN_USER;
import static com.dev.moim.global.common.code.status.SuccessStatus._OK;

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

        authenticate(request, response);
    }

    public void authenticate (
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        OAuthLoginRequest oAuthLoginRequest = HttpRequestUtil.readBody(request, OAuthLoginRequest.class);

        log.info("provider : {}", oAuthLoginRequest.provider());
        log.info("idToken : {}", oAuthLoginRequest.idToken());

        OIDCDecodePayload oidcDecodePayload = oidcService.getOIDCDecodePayload(oAuthLoginRequest.provider(), oAuthLoginRequest.idToken());

        log.info("[payload] email : {}", oidcDecodePayload.email());
        log.info("[payload] sub (사용자 고유 식별자 키) : {}", oidcDecodePayload.sub());

        Optional<User> user = userRepository.findByProviderIdAndProvider(oidcDecodePayload.sub(), oAuthLoginRequest.provider());

        if (user.isPresent()) {
            PrincipalDetails principalDetails = new PrincipalDetails(user.get());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            handleExistingUser(response, principalDetails);
        } else {
            log.info("신규 유저 : 추가 정보 입력 필요");
            HttpResponseUtil.setSuccessResponse(response, UNREGISTERED_OAUTH_LOGIN_USER, null);
        }
    }

    private void handleExistingUser(HttpServletResponse response, PrincipalDetails principalDetails) throws IOException {

        String accessToken = jwtUtil.createAccessToken(principalDetails);
        String refreshToken = jwtUtil.createRefreshToken(principalDetails);

        redisUtil.setValue(principalDetails.user().getId().toString(), refreshToken, jwtUtil.getRefreshTokenValiditySec());

        HttpResponseUtil.setSuccessResponse(response, _OK, new TokenResponse(accessToken, refreshToken));
    }
}