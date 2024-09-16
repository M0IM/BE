package com.dev.moim.domain.user.dto;

import com.dev.moim.global.validation.annotation.ProfileOwnerValidation;

public record UpdateMoimProfileDTO(
        @ProfileOwnerValidation Long profileId
) {
}
