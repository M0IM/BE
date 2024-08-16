package com.dev.moim.domain.moim.dto;

import com.dev.moim.global.validation.annotation.CheckAdminValidation;
import com.dev.moim.global.validation.annotation.UserMoimValidaton;

import java.util.List;

public record UpdateMoimDTO(
         @CheckAdminValidation
         Long moimId,
         String title,
         String address,
         String category,
         String description,
         List<String> imageKeyNames
) {
}
