package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.enums.PostType;
import com.dev.moim.global.validation.annotation.CheckAdminValidation;

import java.util.List;

public record AnnouncementRequestDTO(
        @CheckAdminValidation
        Long moimId,
        String title,
        String content,
        List<String> imageKeyNames,
        List<Long> userIds
) {
}
