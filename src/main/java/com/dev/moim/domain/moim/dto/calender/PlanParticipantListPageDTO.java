package com.dev.moim.domain.moim.dto.calender;

import java.util.List;

public record PlanParticipantListPageDTO(
        List<PlanParticipantListDTO> planParticipantList,
        Boolean isFirst,
        Boolean hasNext
) {
}
