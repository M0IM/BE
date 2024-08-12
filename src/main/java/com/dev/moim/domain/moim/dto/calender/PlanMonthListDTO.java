package com.dev.moim.domain.moim.dto.calender;

import java.util.Map;

public record PlanMonthListDTO<T>(
        Map<Integer, T> planList
) {
}
