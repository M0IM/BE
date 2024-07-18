package com.dev.moim.global.security.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.security.feign.dto.KakaoUserInfo;
import com.dev.moim.global.security.feign.dto.OAuthUserInfo;
import com.dev.moim.global.security.feign.request.KakaoFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.dev.moim.domain.account.entity.enums.Provider.KAKAO;
import static com.dev.moim.domain.account.entity.enums.Role.ROLE_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLoginService implements OAuthLoginService {

    private final KakaoFeign kakaoFeign;
    private final UserRepository userRepository;

    @Override
    public OAuthUserInfo getUserInfo(String oAuthToken) {
        return kakaoFeign.getUserInfo("Bearer " + oAuthToken);
    }

    @Override
    public User findOrCreateUser(OAuthUserInfo oAuthUserInfo) {
        KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) oAuthUserInfo;

        log.info("[KAKAO] providerId = {}", kakaoUserInfo.getProviderId());

        User user = userRepository.findByProviderIdAndProvider(kakaoUserInfo.getProviderId(), KAKAO)
                .orElseGet(() -> User.builder()
                        .email(kakaoUserInfo.getKakaoAccount().getEmail())
                        .nickname(kakaoUserInfo.getKakaoAccount().getProfile().getNickname())
                        .role(ROLE_USER)
                        .provider(KAKAO)
                        .providerId((kakaoUserInfo.getProviderId()))
                        .build());

        return userRepository.save(user);
    }
}
