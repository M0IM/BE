package com.dev.moim.domain.account.service;

import com.dev.moim.domain.account.dto.*;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.account.entity.enums.Provider;
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

import static com.dev.moim.domain.account.entity.enums.UserRole.ROLE_USER;
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

        String encodedPassword = (request.provider() == Provider.LOCAL && request.password() != null)
                ? passwordEncoder.encode(request.password())
                : null;

        User user = User.builder()
                .provider(request.provider())
                .providerId(request.providerId())
                .deviceId(request.fcmToken())
                .email(request.email())
                .password(encodedPassword)
                .userRole(ROLE_USER)
                .userProfileList(new ArrayList<>())
                .gender(request.gender())
                .birth(request.birth())
                .build();

        UserProfile userProfile = UserProfile.builder()
                .name(request.nickname())
                .residence(request.residence())
                .profileType(ProfileType.MAIN)
                .build();

        userProfile.addUser(user);

        User newUser = userRepository.save(user);

        PrincipalDetails principalDetails = new PrincipalDetails(newUser);

        String accessToken = jwtUtil.createAccessToken(principalDetails);
        String refreshToken = jwtUtil.createRefreshToken(principalDetails);

        redisUtil.setValue(principalDetails.user().getId().toString(), refreshToken, jwtUtil.getRefreshTokenValiditySec());

        return new TokenResponse(accessToken, refreshToken, request.provider());
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

            return new TokenResponse(newAccess, newRefresh, principalDetails.getProvider());
        } catch (IllegalArgumentException e) {
            throw new AuthException(AUTH_INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new AuthException(AUTH_EXPIRED_TOKEN);
        }
    }

    public EmailVerificationCodeDTO sendCode(EmailDTO request) {
        try {
            String code = emailUtil.sendMessage(request.email());
            return new EmailVerificationCodeDTO(request.email(), code);
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

    @Transactional
    public void quit(User user) {
        redisUtil.deleteValue(user.getId().toString());
        userRepository.delete(user);
    }

    @Transactional
    public void updatePassword(UpdatePasswordDTO request) {
        User user = userRepository.findByEmailAndProvider(request.email(), Provider.LOCAL)
                        .orElseThrow(() -> new AuthException(USER_NOT_FOUND));

        user.updatePassword(passwordEncoder.encode(request.password()));
    }

    @Transactional
    public void fcmSignOut(User user) {
        user.fcmSignOut();
    }
}
