package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import com.dev.moim.global.validation.annotation.CheckAdminValidation;

public record UpdateMoimDTO(
         @CheckAdminValidation
         Long moimId,
         MoimCategory moimCategory,
         String title,
         String address,
         String category,
         String description,
         String imageKeyName
) {
}
