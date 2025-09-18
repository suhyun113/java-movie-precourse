package com.example.movie.service;

import com.example.movie.domain.Reservation;
import com.example.movie.domain.Screening;
import com.example.movie.policy.MovieDayDiscountPolicy;
import com.example.movie.policy.TimeDiscountPolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceService {
    private final MovieDayDiscountPolicy movieDayDiscountPolicy = new MovieDayDiscountPolicy();
    private final TimeDiscountPolicy timeDiscountPolicy = new TimeDiscountPolicy();

    public int calculatePrice(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) return 0;

        // 상영별 기본가 합산
        Map<Screening, Integer> baseByScreening = new HashMap<>();
        for (Reservation r : reservations) {
            int base = r.getSeats().stream()
                    .mapToInt(seat -> seat.getGrade().getPrice())
                    .sum();
            baseByScreening.merge(r.getScreening(), base, Integer::sum);
        }

        // 상영별로 할인 1회 적용(무비데이% → 시간대 정액)
        int total = 0;
        for (Map.Entry<Screening, Integer> e : baseByScreening.entrySet()) {
            Screening sc = e.getKey();
            int amount = e.getValue();

            int movieDayDiscount = movieDayDiscountPolicy.calculateDiscountAmount(sc, amount);
            amount -= movieDayDiscount;

            int timeDiscount = timeDiscountPolicy.calculateDiscountAmount(sc, amount);
            amount -= timeDiscount;

            total += amount;
        }
        return total;
    }
}
