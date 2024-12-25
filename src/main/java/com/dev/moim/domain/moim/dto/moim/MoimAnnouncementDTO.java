package com.dev.moim.domain.moim.dto.moim;

import java.time.LocalDateTime;

public record MoimAnnouncementDTO(
        String title,
        String content,
        LocalDateTime createAt
) {
}
