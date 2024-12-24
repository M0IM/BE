package com.dev.moim.domain.moim.dto.profile;

import com.dev.moim.domain.moim.entity.enums.VisibilityStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateUserMoimProfileDTO(
        @NotNull String nickname,
        String imageKey,
        String introduction,
        @NotNull VisibilityStatus genderVisibility,
        @NotNull VisibilityStatus residenceVisibility,
        @NotNull VisibilityStatus birthVisibility
) {
}
