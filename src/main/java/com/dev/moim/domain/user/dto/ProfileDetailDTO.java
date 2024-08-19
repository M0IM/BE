package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProfileDetailDTO(
        Long userId,
        String imageUrl,
        String nickname,
        String residence,
        LocalDate birth,
        LocalDateTime createdAt,
        double rating,
        String introduction
) {
        public static ProfileDetailDTO from(User user, UserProfile userProfile, String imageUrl) {
                return new ProfileDetailDTO(
                        user.getId(),
                        imageUrl,
                        userProfile.getName(),
                        userProfile.getResidence(),
                        user.getBirth(),
                        userProfile.getCreatedAt(),
                        user.getRating(),
                        userProfile.getIntroduction()
                );
        }
}
