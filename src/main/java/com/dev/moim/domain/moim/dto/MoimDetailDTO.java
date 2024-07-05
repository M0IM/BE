package com.dev.moim.domain.moim.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MoimDetailDTO(
        Long moimId,
        String title,
        String description,
        List<String>category,
        String address,
        Boolean status,
        List<MoimCalendarDTO> moimCalendarDTOList,
        List<MoimAnnouncementPreviewDTO> moimPostPreviewDTOList,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
}
