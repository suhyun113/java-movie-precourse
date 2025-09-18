package com.example.movie.policy;

import com.example.movie.domain.Movie;
import com.example.movie.domain.Screening;
import com.example.movie.domain.Theater;
import com.example.movie.global.SystemConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscountPolicyTest {
    private Screening movieDayScreening;
    private Screening notMovieDayScreening;
    private Screening earlyMorningScreening;
    private Screening lateNightScreening;
    private Screening basicTimeScreening;
    private final int basePrice = 20000; //  영화 가격

    @BeforeEach
    void setUp() {
        LocalDate releaseDate = LocalDate.of(2025, 9, 10);
        LocalDate finishDate = LocalDate.of(2025, 9, 30);
        Movie movie = new Movie("F1 더무비", Duration.ofMinutes(120), releaseDate, finishDate);
        Theater theater = new Theater(1, 9, 25, 16, 20);

        // 무비데이(20일) 상영
        movieDayScreening = new Screening(movie, theater, LocalDateTime.of(2025, 9, SystemConstants.MOVIE_DAY_DATE_2, 15, 0));
        // 무비데이 아님(21일) 상영
        notMovieDayScreening = new Screening(movie, theater, LocalDateTime.of(2025, 9, 21, 15, 0));

        // 시간대 할인 (오전 11시 이전)
        earlyMorningScreening = new Screening(movie, theater, LocalDateTime.of(2025, 9, 21, 10, 0)); // 10:00
        // 시간대 할인 (오후 8시 이후)
        lateNightScreening = new Screening(movie, theater, LocalDateTime.of(2025, 9, 21, 22, 0)); // 22:00
        // 시간대 할인 아님 (오전 11시 ~ 오후 8시)
        basicTimeScreening = new Screening(movie, theater, LocalDateTime.of(2025, 9, 21, 15, 0)); // 15:00
    }

    @Test
    @DisplayName("무비데이 할인이 올바르게 적용된다.")
    void movieDayDiscountPolicy_applies_correctly() {
        MovieDayDiscountPolicy policy = new MovieDayDiscountPolicy();
        int discountAmount = policy.calculateDiscountAmount(movieDayScreening, basePrice);

        assertThat(discountAmount).isEqualTo((int) (basePrice * SystemConstants.MOVIE_DAY_DISCOUNT_RATE));
    }

    @Test
    @DisplayName("무비데이 조건에 해당하지 않으면 할인이 적용되지 않는다.")
    void movieDayDiscountPolicy_no_discount_applies() {
        MovieDayDiscountPolicy policy = new MovieDayDiscountPolicy();
        int discountAmount = policy.calculateDiscountAmount(notMovieDayScreening, basePrice);

        assertThat(discountAmount).isEqualTo(0);
    }

    @Test
    @DisplayName("시간대 할인이 오전 조건에 올바르게 적용된다.")
    void timeDiscountPolicy_applies_morning_discount() {
        TimeDiscountPolicy policy = new TimeDiscountPolicy();
        int discountAmount = policy.calculateDiscountAmount(earlyMorningScreening, basePrice);

        assertThat(discountAmount).isEqualTo(SystemConstants.TIME_SLOT_DISCOUNT_AMOUNT);
    }

    @Test
    @DisplayName("시간대 할인이 오후 조건에 올바르게 적용된다.")
    void timeDiscountPolicy_applies_late_night_discount() {
        TimeDiscountPolicy policy = new TimeDiscountPolicy();
        int discountAmount = policy.calculateDiscountAmount(lateNightScreening, basePrice);

        assertThat(discountAmount).isEqualTo(SystemConstants.TIME_SLOT_DISCOUNT_AMOUNT);
    }

    @Test
    @DisplayName("일반 시간대에는 할인이 적용되지 않는다.")
    void timeDiscountPolicy_no_discount_applies() {
        TimeDiscountPolicy policy = new TimeDiscountPolicy();
        int discountAmount = policy.calculateDiscountAmount(basicTimeScreening, basePrice);

        assertThat(discountAmount).isEqualTo(0);
    }
}
