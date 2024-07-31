package com.dev.moim.global.security.service;

import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.security.feign.request.NaverFeign;
import com.dev.moim.global.security.dto.NaverUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverLoginService {

    private final NaverFeign naverFeign;

    public NaverUserInfo getUserInfo(String oAuthToken) {

        try {
            NaverUserInfo naverUserInfo = naverFeign.getUserInfo("bearer " + oAuthToken);

            return naverUserInfo;
        } catch (Exception e) {
            throw new AuthException(ErrorStatus.OAUTH_INVALID_TOKEN);
        }
    }
}
