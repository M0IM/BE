package com.dev.moim.domain.moim;

import java.time.LocalDateTime;

public record CalenderSchedule(
        String title,
        LocalDateTime startTime,
        boolean notification,
        LocalDateTime notificationTime
) {
}
