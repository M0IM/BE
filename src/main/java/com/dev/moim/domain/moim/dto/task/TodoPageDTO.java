package com.dev.moim.domain.moim.dto.task;

import java.util.List;

public record TodoPageDTO(
        List<?> list,
        Long nextCursor,
        Boolean hasNext
) {
}
