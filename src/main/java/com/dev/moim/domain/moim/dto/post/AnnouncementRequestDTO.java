package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.enums.PostType;
import com.dev.moim.global.validation.annotation.CheckAdminValidation;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record AnnouncementRequestDTO(
        @CheckAdminValidation
        Long moimId,
        @Length(min = 1, max = 255)
        String title,
        @Length(min = 1, max = 2000)
        String content,
        List<String> imageKeyNames,
        List<Long> userIds,
        Boolean isAllUserSelected
) {
}
