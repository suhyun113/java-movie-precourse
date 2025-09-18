package com.example.movie.policy;

import com.example.movie.domain.Screening;
import com.example.movie.global.SystemConstants;

import java.time.LocalDate;

public class MovieDayDiscountPolicy implements DiscountPolicy {
    @Override
    public int calculateDiscountAmount(Screening screening, int price) {
        LocalDate screeningDate = screening.getStartDateTime().toLocalDate();
        int dayOfMonth = screeningDate.getDayOfMonth(); // 월 구하기

        if (dayOfMonth == SystemConstants.MOVIE_DAY_DATE_1 ||
                dayOfMonth == SystemConstants.MOVIE_DAY_DATE_2 ||
                dayOfMonth == SystemConstants.MOVIE_DAY_DATE_3) {

            return (int) (price * SystemConstants.MOVIE_DAY_DISCOUNT_RATE);
        }
        return 0;
    }
}
