package com.dev.moim.global.security.filter;

import com.dev.moim.domain.account.dto.SocialLoginRequest;
import com.dev.moim.domain.account.dto.TokenResponse;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.redis.service.RefreshTokenService;
import com.dev.moim.global.security.feign.dto.KakaoUserInfo;
import com.dev.moim.global.security.feign.request.KakaoFeign;
import com.dev.moim.global.security.principal.PrincipalDetails;
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

import static com.dev.moim.domain.account.entity.enums.Provider.KAKAO;
import static com.dev.moim.domain.account.entity.enums.Role.ROLE_USER;
import static com.dev.moim.global.common.code.status.ErrorStatus._BAD_REQUEST;

@Slf4j
public class OAuth2LoginFilter extends AbstractAuthenticationProcessingFilter {

    private final KakaoFeign kakaoFeign;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public OAuth2LoginFilter(KakaoFeign kakaoFeign, UserRepository userRepository, JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        super(new AntPathRequestMatcher("/api/v1/auth/oauth/kakao"));
        this.kakaoFeign = kakaoFeign;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public Authentication attemptAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response) throws IOException, ServletException {

        log.info("** OAuth2LoginFilter **");

        SocialLoginRequest socialLoginRequest = readBody(request);

        log.info("oAuthToken = {}", socialLoginRequest.oAuthToken());

        KakaoUserInfo kakaoUserInfo = kakaoFeign.getUserInfo("Bearer " + socialLoginRequest.oAuthToken());

        User user = userRepository.findByProviderIdAndProvider(kakaoUserInfo.getId(), KAKAO)
                .orElseGet(() -> createNewKakaoUser(kakaoUserInfo));

        PrincipalDetails principalDetails = new PrincipalDetails(user);

        String accessToken = jwtUtil.createAccessToken(principalDetails);
        String refreshToken = jwtUtil.createRefreshToken(principalDetails);

        refreshTokenService.saveToken(principalDetails.user().getEmail(), refreshToken, jwtUtil.getRefreshTokenValiditySec());

        HttpResponseUtil.setSuccessResponse(response, HttpStatus.CREATED, new TokenResponse(accessToken, refreshToken));

        return null;
    }

    private SocialLoginRequest readBody(HttpServletRequest request) {
        SocialLoginRequest requestDTO = null;
        ObjectMapper om = new ObjectMapper();

        try {
            requestDTO = om.readValue(request.getInputStream(), SocialLoginRequest.class);
        } catch (IOException e) {
            throw new AuthException(_BAD_REQUEST);
        }

        return requestDTO;
    }

    private User createNewKakaoUser(KakaoUserInfo kakaoUserInfo) {
        User user = User.builder()
                .email(kakaoUserInfo.getKakaoAccount().getEmail())
                .nickname(kakaoUserInfo.getKakaoAccount().getProfile().getNickname())
                .role(ROLE_USER)
                .provider(KAKAO)
                .build();
        return userRepository.save(user);
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
