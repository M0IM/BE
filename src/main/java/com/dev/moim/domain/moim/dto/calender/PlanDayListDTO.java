package com.dev.moim.domain.moim.dto.calender;

import java.util.List;

public record PlanDayListDTO(
        int memberWithPlanCnt,
        List<MoimPlanDTO> planList
) {
}
