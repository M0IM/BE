package com.dev.moim.domain.moim.dto.profile;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.Gender;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GetUserMoimProfileDTO(
        Long userMoimId,
        String nickname,
        String imageUrl,
        String introduction,
        MoimRole moimRole,
        JoinStatus joinStatus,
        @Schema(description = "모임 가입 날짜")
        LocalDateTime createdAt,
        Long userId,
        String residence,
        LocalDate birth,
        Gender gender,
        double rating,
        int participateMoimCnt
) {
    public static GetUserMoimProfileDTO from(UserMoim userMoim, User user, int participateMoimCnt) {
        return new GetUserMoimProfileDTO(
                userMoim.getId(),
                userMoim.getNickname(),
                user.getImageUrl(),
                userMoim.getIntroduction(),
                userMoim.getMoimRole(),
                userMoim.getJoinStatus(),
                userMoim.getCreatedAt(),
                user.getId(),
                user.getResidence(),
                user.getBirth(),
                user.getGender(),
                user.getRating(),
                participateMoimCnt
        );
    }
}
