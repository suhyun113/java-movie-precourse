package com.example.movie.service;

import com.example.movie.domain.Reservation;
import com.example.movie.domain.Screening;
import com.example.movie.policy.MovieDayDiscountPolicy;
import com.example.movie.policy.TimeDiscountPolicy;

import java.util.List;

public class PriceService {
    private final MovieDayDiscountPolicy movieDayDiscountPolicy = new MovieDayDiscountPolicy();
    private final TimeDiscountPolicy timeDiscountPolicy = new TimeDiscountPolicy();

    // 가격 계산
    public int calculatePrice(List<Reservation> reservations) {
        int totalBasePrice = calculateBasePrice(reservations);
        int finalPrice = totalBasePrice;

        if (reservations.isEmpty()) {
            return 0;
        }

        // 1. 무비데이 할인 (비율) 적용
        Screening firstScreening = reservations.get(0).getScreening();
        int movieDayDiscount = movieDayDiscountPolicy.calculateDiscountAmount(firstScreening, finalPrice);
        finalPrice -= movieDayDiscount;

        // 2. 시간대 할인 (정액) 적용
        int timeDiscount = timeDiscountPolicy.calculateDiscountAmount(firstScreening, finalPrice);
        finalPrice -= timeDiscount;

        return finalPrice;
    }

    private int calculateBasePrice(List<Reservation> reservations) {
        return reservations.stream()
                .flatMap(reservation -> reservation.getSeats().stream())
                .mapToInt(seat -> seat.getGrade().getPrice())
                .sum();
    }
}