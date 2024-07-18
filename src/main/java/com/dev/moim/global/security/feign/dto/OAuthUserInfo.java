package com.dev.moim.global.security.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OAuthUserInfo {
    private Long providerId;
    private String email;
    private String nickname;
}
