package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;

import java.time.LocalDateTime;

public record ProfileDetailDTO(
        Long userId,
        String imageUrl,
        String nickname,
        String residence,
        String birth,
        LocalDateTime createdAt,
        double rating,
        String introduction
) {
        public static ProfileDetailDTO from(User user, UserProfile userProfile) {
                return new ProfileDetailDTO(
                        user.getId(),
                        userProfile.getImageFileName(),
                        userProfile.getName(),
                        userProfile.getResidence(),
                        userProfile.getBirth(),
                        userProfile.getCreatedAt(),
                        user.getRating(),
                        userProfile.getIntroduction()
                );
        }
}
