package com.dev.moim.global.security.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.global.security.feign.dto.OAuthUserInfo;

public interface OAuthLoginService {
    OAuthUserInfo getUserInfo(String oAuthToken);

    User findOrCreateUser(OAuthUserInfo oAuthUserInfo);
}
