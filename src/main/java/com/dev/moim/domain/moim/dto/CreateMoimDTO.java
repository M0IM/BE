package com.dev.moim.domain.moim.dto;

import java.util.List;

public record CreateMoimDTO(
        String title,
        String address,
        String category,
        String description,
        List<String>imageUrl,
        String tag
) {

}
