package com.dev.moim.domain.moim.dto.task;

import java.util.List;

public record TodoAssigneeListForAdminDTO(
        List<TodoAssigneeDetailDTO> todoAssigneeDetailDTOList,
        Long nextCursor,
        Boolean hasNext
) {
}
