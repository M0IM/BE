package com.dev.moim.domain.account.service;

import com.dev.moim.domain.account.dto.JoinRequest;
import com.dev.moim.domain.account.dto.JoinResponse;
import com.dev.moim.domain.account.dto.SocialLoginRequest;
import com.dev.moim.domain.account.dto.TokenResponse;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.Gender;
import com.dev.moim.domain.account.entity.enums.Role;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.redis.service.RefreshTokenService;
import com.dev.moim.global.security.feign.dto.KakaoUserInfo;
import com.dev.moim.global.security.feign.request.KakaoFeign;
import com.dev.moim.global.security.principal.PrincipalDetails;
import com.dev.moim.global.security.principal.PrincipalDetailsService;
import com.dev.moim.global.security.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

import static com.dev.moim.domain.account.entity.enums.Provider.KAKAO;
import static com.dev.moim.domain.account.entity.enums.Provider.LOCAL;
import static com.dev.moim.domain.account.entity.enums.Role.ROLE_USER;
import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final PrincipalDetailsService principalDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final KakaoFeign kakaoFeign;

    @Transactional
    public JoinResponse join(JoinRequest request) {

        validateEmailDuplication(request.email());

        Role role = Optional.ofNullable(request.role())
                .filter(roleStr -> !roleStr.isEmpty())
                .map(Role::valueOf)
                .orElse(ROLE_USER);

        User user = User.builder()
                .nickname(request.nickname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .provider(LOCAL)
                .userProfileList(new ArrayList<>())
                .build();

        UserProfile userProfile = UserProfile.builder()
                .gender(Gender.from(request.gender()))
                .birth(request.birth())
                .introduction(request.introduction())
                .residence(request.residence())
                .build();

        user.addUserProfile(userProfile);

        userRepository.save(user);

        return JoinResponse.of(user);
    }

    private void validateEmailDuplication(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AuthException(EMAIL_DUPLICATION);
        }
    }

    public TokenResponse kakaoLogin(SocialLoginRequest request) {

        log.info("kakaoToken = {}", request.oAuthToken());

        KakaoUserInfo kakaoUserInfo = kakaoFeign.getUserInfo("Bearer " + request.oAuthToken());

        log.info("id = {}", kakaoUserInfo.getId());
        log.info("emailNeedsAgreement = {}", kakaoUserInfo.getKakaoAccount().isEmailNeedsAgreement());
        log.info("email = {}", kakaoUserInfo.getKakaoAccount().getEmail());
        log.info("nickname = {}", kakaoUserInfo.getKakaoAccount().getProfile().getNickname());
        log.info("thumbnailImageUrl = {}", kakaoUserInfo.getKakaoAccount().getProfile().getThumbnailImageUrl());

        User user = userRepository.findByProviderIdAndProvider(kakaoUserInfo.getId(), KAKAO)
                .orElseGet(() -> createNewKakaoUser(kakaoUserInfo));

        PrincipalDetails principalDetails = new PrincipalDetails(user);

        String accessToken = jwtUtil.createAccessToken(principalDetails);
        String refreshToken = jwtUtil.createRefreshToken(principalDetails);

        refreshTokenService.saveToken(principalDetails.user().getEmail(), refreshToken, jwtUtil.getRefreshTokenValiditySec());

        return new TokenResponse(accessToken, refreshToken);
    }

    private User createNewKakaoUser(KakaoUserInfo kakaoUserInfo) {
        User user = User.builder()
                .email(kakaoUserInfo.getKakaoAccount().getEmail())
                .nickname(kakaoUserInfo.getKakaoAccount().getProfile().getNickname())
                .role(ROLE_USER)
                .provider(KAKAO)
                .userProfileList(new ArrayList<>())
                .build();

        UserProfile userProfile = UserProfile.builder()
                .build();

        user.addUserProfile(userProfile);

        return userRepository.save(user);
    }

    public TokenResponse reissueToken(String refreshToken) {
        try {
            if (!jwtUtil.isTokenValid(refreshToken)) {
                throw new AuthException(AUTH_INVALID_TOKEN);
            }

            refreshTokenService.validateRefreshToken(refreshToken);
            refreshTokenService.deleteToken(refreshToken);

            PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(jwtUtil.getEmail(refreshToken));

            String newAccess = jwtUtil.createAccessToken(principalDetails);
            String newRefresh = jwtUtil.createRefreshToken(principalDetails);

            refreshTokenService.saveToken(principalDetails.user().getEmail(), newRefresh, jwtUtil.getRefreshTokenValiditySec());

            return new TokenResponse(newAccess, newRefresh);
        } catch (IllegalArgumentException e) {
            throw new AuthException(AUTH_INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new AuthException(AUTH_EXPIRED_TOKEN);
        }
    }

    public void logout(String email) {
        refreshTokenService.deleteToken(email);
    }
}
