package com.dev.moim.global.security.filter;

import com.dev.moim.domain.account.dto.OAuthLoginRequest;
import com.dev.moim.domain.account.dto.TokenResponse;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.redis.util.RedisUtil;
import com.dev.moim.global.security.principal.PrincipalDetails;
import com.dev.moim.global.security.util.HttpRequestUtil;
import com.dev.moim.global.security.util.HttpResponseUtil;
import com.dev.moim.global.security.util.JwtUtil;
import com.dev.moim.global.security.util.OAuthAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Optional;

import static com.dev.moim.global.common.code.status.SuccessStatus.UNREGISTERED_OAUTH_LOGIN_USER;
import static com.dev.moim.global.common.code.status.SuccessStatus._OK;

@Slf4j
public class OAuthLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public OAuthLoginFilter(JwtUtil jwtUtil, RedisUtil redisUtil, AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(new AntPathRequestMatcher("/api/v1/auth/oAuth"));
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response) throws IOException, ServletException {

        log.info("** OAuthLoginFilter **");

        OAuthLoginRequest oAuthLoginRequest = HttpRequestUtil.readBody(request, OAuthLoginRequest.class);

        log.info("provider : {}", oAuthLoginRequest.provider());
        log.info("idToken : {}", oAuthLoginRequest.idToken());

        OAuthAuthenticationToken authRequest = new OAuthAuthenticationToken(oAuthLoginRequest.provider(), null, oAuthLoginRequest.idToken());

        return authenticationManager.authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain,
            @NonNull Authentication authResult) throws IOException, ServletException {

        OAuthAuthenticationToken authentication = (OAuthAuthenticationToken) authResult;

        Optional<User> user = userRepository.findByProviderIdAndProvider(authentication.getProviderId(), authentication.getProvider());

        if (user.isPresent()) {
            User existingUser = user.get();
            PrincipalDetails principalDetails = new PrincipalDetails(existingUser);

            String accessToken = jwtUtil.createAccessToken(principalDetails);
            String refreshToken = jwtUtil.createRefreshToken(principalDetails);

            redisUtil.setValue(principalDetails.user().getId().toString(), refreshToken, jwtUtil.getRefreshTokenValiditySec());

            HttpResponseUtil.setSuccessResponse(response, _OK, new TokenResponse(accessToken, refreshToken));
        } else {
            log.info("신규 유저 : 추가 정보 입력 필요");
            HttpResponseUtil.setSuccessResponse(response, UNREGISTERED_OAUTH_LOGIN_USER, authentication.getCredentials());
        }
    }
}