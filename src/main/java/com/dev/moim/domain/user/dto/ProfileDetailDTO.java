package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.Gender;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.account.entity.enums.Provider;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProfileDetailDTO(
        Long userId,
        Long profileId,
        ProfileType profileType,
        Provider provider,
        String imageUrl,
        String nickname,
        String residence,
        LocalDate birth,
        Gender gender,
        LocalDateTime createdAt,
        double rating,
        int participateMoimCnt,
        String introduction
) {
        public static ProfileDetailDTO from(User user, UserProfile userProfile, int participateMoimCnt) {
                return new ProfileDetailDTO(
                        user.getId(),
                        userProfile.getId(),
                        userProfile.getProfileType(),
                        user.getProvider(),
                        userProfile.getImageUrl()!= null && !userProfile.getImageUrl().isEmpty() ? userProfile.getImageUrl() : null,
                        userProfile.getName(),
                        user.getResidence(),
                        user.getBirth(),
                        user.getGender(),
                        userProfile.getCreatedAt(),
                        user.getRating(),
                        participateMoimCnt,
                        userProfile.getIntroduction()
                );
        }
}
