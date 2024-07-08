package com.dev.moim.domain.account.service;

import com.dev.moim.domain.account.dto.ReissueTokenResponse;
import com.dev.moim.domain.account.dto.JoinRequest;
import com.dev.moim.domain.account.dto.JoinResponse;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.Gender;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.error.handler.AuthException;
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

    @Transactional
    public JoinResponse join(JoinRequest request) {

        if(userRepository.existsByEmail(request.email())){
            throw new AuthException(EMAIL_DUPLICATION);
        }

        User user = User.builder()
                .nickname(request.nickname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
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

    public ReissueTokenResponse reissueToken(String refreshToken) {
        try {
            if (!jwtUtil.isTokenValid(refreshToken)) {
                throw new AuthException(AUTH_INVALID_TOKEN);
            }

            refreshTokenService.validateRefreshToken(refreshToken);

            PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(jwtUtil.getEmail(refreshToken));

            return new ReissueTokenResponse(
                    jwtUtil.createAccessToken(principalDetails),
                    jwtUtil.createRefreshToken(principalDetails)
            );
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
