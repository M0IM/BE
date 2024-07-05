package com.dev.moim.domain.moim.dto;

public record MoimAnnouncementPreviewDTO(
        Long announcementId,
        String title,
        String content,
        String writer
) {
}
