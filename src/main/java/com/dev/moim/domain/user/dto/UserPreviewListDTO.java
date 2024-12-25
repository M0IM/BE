package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.moim.entity.UserMoim;
import org.springframework.data.domain.Slice;

import java.util.List;

public record UserPreviewListDTO(
        List<UserPreviewDTO> userPreviewDTOList,
        Boolean hasNext,
        Long nextCursor
) {

    public static UserPreviewListDTO from(Slice<UserMoim> slice, Long nextCursor) {
        List<UserPreviewDTO> userPreviewDTOList = slice.stream()
                .map(UserPreviewDTO::from)
                .toList();

        return new UserPreviewListDTO(
                userPreviewDTOList,
                slice.hasNext(),
                nextCursor);
    }
}
