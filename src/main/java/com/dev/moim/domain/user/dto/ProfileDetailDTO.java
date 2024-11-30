package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.User;
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
        public static ProfileDetailDTO from(User user,  int participateMoimCnt) {
                return new ProfileDetailDTO(
                        user.getId(),
                        null,
                        null,
                        user.getProvider(),
                        user.getImageUrl()!= null && !user.getImageUrl().isEmpty() ? user.getImageUrl() : null,
                        user.getNickname(),
                        user.getResidence(),
                        user.getBirth(),
                        user.getGender(),
                        user.getCreatedAt(),
                        user.getRating(),
                        participateMoimCnt,
                        user.getIntroduction()
                );
        }
}
