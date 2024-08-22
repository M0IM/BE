package com.dev.moim.global.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class TimeUtil {
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return Optional.ofNullable(timestamp)
                .map(ts -> ts.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .orElse(null);
    }
}