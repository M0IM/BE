package com.dev.moim.domain.account.service;

import com.dev.moim.domain.account.dto.ReissueTokenResponse;
import com.dev.moim.domain.account.dto.SignUpRequest;
import com.dev.moim.domain.account.dto.SignUpResponse;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.Gender;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.security.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dev.moim.global.common.code.status.ErrorStatus.EMAIL_DUPLICATION;
import static com.dev.moim.global.common.code.status.ErrorStatus.NOT_EQUAL_TOKEN;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {

        if(userRepository.existsByEmail(request.email())){
            throw new AuthException(EMAIL_DUPLICATION);
        }

        User user = User.builder()
                .nickname(request.nickname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        UserProfile userProfile = UserProfile.builder()
                .gender(Gender.from(request.gender()))
                .birth(request.birth())
                .introduction(request.introduction())
                .residence(request.residence())
                .build();

        userProfile.setUser(user);

        userRepository.save(user);

        log.info("userId = {}", user.getId());

        return SignUpResponse.of(user);
    }

    @Transactional
    public ReissueTokenResponse reissueToken(String refreshToken) {
        jwtProvider.isTokenValid(refreshToken);

        if (!refreshTokenService.validateRefreshToken(refreshToken)) {
            throw new AuthException(NOT_EQUAL_TOKEN);
        }

        return refreshTokenService.reissueToken(refreshToken);
    }

    public void logout(String email) {
        refreshTokenService.deleteToken(email);
    }
}
