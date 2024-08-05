package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ProfileDetailDTO(
        Long profileId,
        String profileImageUrl,
        String nickname,
        @Schema(description = "프로필 타입", defaultValue = "SUB", allowableValues = {"MAIN", "SUB"})
        ProfileType profileType,
        @Schema(description = "모임 내의 유저 역할", defaultValue = "MEMBER", allowableValues = {"OWNER", "ADMIN", "MEMBER"})
        MoimRole moimRole,
        String residence,
        LocalDate birth,
        LocalDateTime createdAt,
        double rating,
        int joinedMoimCnt,
        List<ReceivedReviewDTO> receivedReviewList,
        String introduction,
        List<JoinedMoimDTO> joinedMoimList
) {
}
