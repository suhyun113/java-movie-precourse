package com.example.movie.policy;

import com.example.movie.domain.Screening;
import com.example.movie.global.SystemConstants;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeDiscountPolicy implements DiscountPolicy {
    @Override
    public int calculateDiscountAmount(Screening screening, int price) {
        LocalTime startTime = screening.getStartDateTime().toLocalTime();

        if (startTime.isBefore(LocalTime.of(SystemConstants.TIME_SLOT_HOUR_BEFORE, 0)) || // 11:00 이전
        startTime.isAfter(LocalTime.of(SystemConstants.TIME_SLOT_HOUR_AFTER, 0))) { // 20:00 이후
            return SystemConstants.TIME_SLOT_DISCOUNT_AMOUNT;
        }
        return 0;
    }
}
