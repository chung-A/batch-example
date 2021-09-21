package com.example.batch.config.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TimeUtils {

    public static long getAverageTime(LocalDateTime startAt, LocalDateTime endAt, long count, TimeUnit unit) {
        BigDecimal base = new BigDecimal(getNanoSec(startAt, endAt))
                .divide(new BigDecimal(count), 9, RoundingMode.UP);
        switch (unit) {
            case NANOSECONDS:
                return TimeUnit.NANOSECONDS.toNanos(base.longValue());
            case MILLISECONDS:
                return TimeUnit.NANOSECONDS.toMillis(base.longValue());
            case SECONDS:
                return TimeUnit.NANOSECONDS.toSeconds(base.longValue());
            case MINUTES:
                return TimeUnit.NANOSECONDS.toMinutes(base.longValue());
            default:
                throw new UnsupportedOperationException();
        }
    }

    private static long getNanoSec(LocalDateTime startAt, LocalDateTime endAt) {
        Duration duration = Duration.between(startAt, endAt);
        return TimeUnit.SECONDS.toNanos(duration.getSeconds()) + (duration.getNano() > 0 ? duration.getNano() : 1);
    }

}
