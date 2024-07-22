package com.dev.moim.global.security.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class KakaoUserInfo extends OAuthUserInfo {

    @JsonProperty("id")
    private Long providerId;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class KakaoAccount {
        private Profile profile;
        @JsonProperty("email_needs_agreement")
        private boolean emailNeedsAgreement;
        private String email;

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        public static class Profile {
            private String nickname;

            @JsonProperty("thumbnail_image_url")
            private String thumbnailImageUrl;

            private String gender;

            private String birthyear;

            private String birthday;
        }
    }

    public KakaoUserInfo(Long providerId, KakaoAccount kakaoAccount) {
        super(providerId, kakaoAccount.getEmail(), kakaoAccount.getProfile().getNickname());
        this.providerId = providerId;
        this.kakaoAccount = kakaoAccount;
    }
}
