package com.dev.moim.domain.account.service;

import com.dev.moim.domain.account.dto.EmailVerificationCodeDTO;
import com.dev.moim.domain.account.dto.EmailVerificationResultDTO;
import com.dev.moim.domain.account.dto.JoinRequest;
import com.dev.moim.domain.account.dto.TokenResponse;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.domain.account.entity.enums.Role;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.email.EmailUtil;
import com.dev.moim.global.error.GeneralException;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.redis.util.RedisUtil;
import com.dev.moim.global.security.principal.PrincipalDetails;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final EmailUtil emailUtil;

    @Transactional
    public TokenResponse join(JoinRequest request) {

        validateEmailDuplication(request.email());

        Role role = Optional.ofNullable(request.role())
                .filter(requestRole -> ! requestRole.isEmpty())
                .map(Role::valueOf)
                .orElse(ROLE_USER);

        Provider provider = Optional.ofNullable(request.provider())
                .orElseThrow(() -> new AuthException(PROVIDER_NOT_FOUND));

        User user = User.builder()
                .nickname(request.nickname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .provider(provider)
                .providerId(request.providerId())
                .userProfileList(new ArrayList<>())
                .build();

        UserProfile userProfile = UserProfile.builder()
                .gender(request.gender())
                .birth(request.birth())
                .residence(request.residence())
                .build();

        user.addUserProfile(userProfile);

        User newUser = userRepository.save(user);

        PrincipalDetails principalDetails = new PrincipalDetails(newUser);

        String accessToken = jwtUtil.createAccessToken(principalDetails);
        String refreshToken = jwtUtil.createRefreshToken(principalDetails);

        redisUtil.setValue(principalDetails.user().getId().toString(), refreshToken, jwtUtil.getRefreshTokenValiditySec());

        return new TokenResponse(accessToken, refreshToken);
    }

    private void validateEmailDuplication(String email) {
        if (userRepository.existsByEmailAndProvider(email, LOCAL)) {
            throw new AuthException(EMAIL_DUPLICATION);
        }
    }

    @Transactional
    public TokenResponse reissueToken(String refreshToken) {
        try {
            if (!jwtUtil.isTokenValid(refreshToken)) {
                throw new AuthException(AUTH_INVALID_TOKEN);
            }

            String userId = jwtUtil.getUserId(refreshToken);
            redisUtil.deleteValue(userId);

            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow((() -> new AuthException(USER_NOT_FOUND)));
            PrincipalDetails principalDetails = new PrincipalDetails(user);

            String newAccess = jwtUtil.createAccessToken(principalDetails);
            String newRefresh = jwtUtil.createRefreshToken(principalDetails);

            redisUtil.setValue(userId, newRefresh, jwtUtil.getRefreshTokenValiditySec());

            return new TokenResponse(newAccess, newRefresh);
        } catch (IllegalArgumentException e) {
            throw new AuthException(AUTH_INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new AuthException(AUTH_EXPIRED_TOKEN);
        }
    }

    public EmailVerificationCodeDTO sendCode(String email) {
        try {
            String code = emailUtil.sendMessage(email);
            return new EmailVerificationCodeDTO(email, code);
        } catch (Exception e) {
            throw new GeneralException(EMAIL_SEND_FAIL);
        }
    }

    public EmailVerificationResultDTO verifyCode(EmailVerificationCodeDTO request) {
        String redisCode = redisUtil.getValue(request.code());
        if (redisCode == null) {
            throw new AuthException(EMAIL_CODE_NOT_FOUND);
        }

       boolean isCodeValid = request.code().equals(redisCode);
        if (isCodeValid) {
            redisUtil.deleteValue(request.email());
        } else {
            throw new AuthException(INCORRECT_EMAIL_CODE);
        }

        return new EmailVerificationResultDTO(request.email(), isCodeValid);
    }
}
