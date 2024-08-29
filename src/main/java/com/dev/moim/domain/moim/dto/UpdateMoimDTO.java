package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import com.dev.moim.global.validation.annotation.CheckAdminValidation;
import org.hibernate.validator.constraints.Length;

public record UpdateMoimDTO(
         @CheckAdminValidation
         Long moimId,
         MoimCategory moimCategory,
         @Length(min = 1, max = 255)
         String title,
         @Length(min = 1, max = 255)
         String address,
         @Length(min = 1, max = 255)
         String description,
         String imageKeyName
) {
}
