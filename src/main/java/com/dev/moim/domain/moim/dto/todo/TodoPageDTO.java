package com.dev.moim.domain.moim.dto.todo;

import java.util.List;

public record TodoPageDTO(
        List<?> list,
        Long nextCursor,
        Boolean hasNext
) {
}
