package com.dev.moim.domain.moim.dto.moim;

public record MoimAnnouncementPreviewDTO(
        Long announcementId,
        String title,
        String content,
        String writer
) {
}
