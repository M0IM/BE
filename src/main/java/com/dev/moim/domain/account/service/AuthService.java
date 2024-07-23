package com.dev.moim.domain.account.service;

import com.dev.moim.domain.account.dto.JoinRequest;
import com.dev.moim.domain.account.dto.JoinResponse;
import com.dev.moim.domain.account.dto.TokenResponse;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.Gender;
import com.dev.moim.domain.account.entity.enums.Role;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.redis.util.RedisUtil;
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

import static com.dev.moim.domain.account.entity.enums.Provider.LOCAL;
import static com.dev.moim.domain.account.entity.enums.Role.ROLE_USER;
import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {

    private final PrincipalDetailsService principalDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

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

    @Transactional
    public TokenResponse reissueToken(String refreshToken) {
        try {
            if (!jwtUtil.isTokenValid(refreshToken)) {
                throw new AuthException(AUTH_INVALID_TOKEN);
            }

            String email = jwtUtil.getEmail(refreshToken);
            redisUtil.deleteValue(email);

            PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(email);

            String newAccess = jwtUtil.createAccessToken(principalDetails);
            String newRefresh = jwtUtil.createRefreshToken(principalDetails);

            redisUtil.setValue(email, newRefresh, jwtUtil.getRefreshTokenValiditySec());

            return new TokenResponse(newAccess, newRefresh);
        } catch (IllegalArgumentException e) {
            throw new AuthException(AUTH_INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new AuthException(AUTH_EXPIRED_TOKEN);
        }
    }
}
