package com.dev.moim.global.security.service;

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

        return naverFeign.getUserInfo("bearer " + oAuthToken);
    }
}
