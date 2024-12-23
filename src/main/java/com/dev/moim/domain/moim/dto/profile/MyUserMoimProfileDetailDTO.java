package com.dev.moim.domain.moim.dto.profile;

import com.dev.moim.domain.account.entity.enums.Gender;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.entity.enums.VisibilityStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MyUserMoimProfileDetailDTO(
        Long userId,
        Long userMoimId,
        String nickname,
        String imageUrl,
        String introduction,
        VisibilityStatus genderVisibility,
        Gender gender,
        VisibilityStatus residenceVisibility,
        String residence,
        VisibilityStatus birthVisibility,
        LocalDate birth,
        MoimRole moimRole,
        LocalDateTime createdAt,
        double rating,
        int participateMoimCnt
) {
    public static MyUserMoimProfileDetailDTO from(UserMoim userMoim, int participateMoimCnt) {
        return new MyUserMoimProfileDetailDTO(
                userMoim.getUser().getId(),
                userMoim.getId(),
                userMoim.getNickname(),
                userMoim.getImageUrl(),
                userMoim.getIntroduction(),
                userMoim.getGenderVisibility(),
                userMoim.getUser().getGender(),
                userMoim.getResidenceVisibility(),
                userMoim.getUser().getResidence(),
                userMoim.getBirthVisibility(),
                userMoim.getUser().getBirth(),
                userMoim.getMoimRole(),
                userMoim.getCreatedAt(),
                userMoim.getUser().getRating(),
                participateMoimCnt
        );
    }
}
