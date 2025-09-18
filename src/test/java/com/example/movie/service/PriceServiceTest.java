package com.example.movie.service;

import com.example.movie.domain.Movie;
import com.example.movie.domain.Reservation;
import com.example.movie.domain.Screening;
import com.example.movie.domain.Seat;
import com.example.movie.enums.SeatGrade;
import com.example.movie.global.SystemConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PriceServiceTest {

    private PriceService priceService;
    private Movie testMovie;
    private Screening testScreening;
    private List<Reservation> reservations;

    @BeforeEach
    void setUp() {
        priceService = new PriceService();
        LocalDate releaseDate = LocalDate.of(2025, 9, 10);
        LocalDate finishDate = LocalDate.of(2025, 9, 30);
        testMovie = new Movie("F1 더무비", Duration.ofMinutes(120), releaseDate, finishDate);
    }

    @Test
    @DisplayName("좌석 등급에 따른 기본 가격을 올바르게 계산한다.")
    void calculatePrice_basePrice_correctly() {
        testScreening = new Screening(testMovie, null, LocalDateTime.of(2025, 9, 15, 15, 0)); // 할인 조건 x
        Seat bSeat = new Seat('A', 1, SeatGrade.B, false);
        Seat aSeat = new Seat('E', 1, SeatGrade.A, false);
        reservations = List.of(
                new Reservation(testScreening, List.of(bSeat)),
                new Reservation(testScreening, List.of(aSeat))
        );
        int totalPrice = priceService.calculatePrice(reservations);

        assertThat(totalPrice).isEqualTo(SeatGrade.B.getPrice() + SeatGrade.A.getPrice());
    }

    @Test
    @DisplayName("무비데이 할인만 적용된다.")
    void calculatePrice_applies_movieDayDiscount_only() {
        // given
        testScreening = new Screening(testMovie, null, LocalDateTime.of(2025, 9, 20, 15, 0)); // 무비데이, 시간대 아님
        Seat sSeat = new Seat('A', 1, SeatGrade.B, false);
        reservations = List.of(new Reservation(testScreening, List.of(sSeat)));
        int expectedPrice = (int) (SeatGrade.B.getPrice() * (1 - SystemConstants.MOVIE_DAY_DISCOUNT_RATE));
        int finalPrice = priceService.calculatePrice(reservations);

        assertThat(finalPrice).isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("시간대 할인만 적용된다.")
    void calculatePrice_applies_timeSlotDiscount_only() {
        testScreening = new Screening(testMovie, null, LocalDateTime.of(2025, 10, 15, 9, 30)); // 무비데이 아님, 오전 시간대
        Seat aSeat = new Seat('A', 1, SeatGrade.B, false);
        reservations = List.of(new Reservation(testScreening, List.of(aSeat)));
        int expectedPrice = SeatGrade.B.getPrice() - SystemConstants.TIME_SLOT_DISCOUNT_AMOUNT;
        int finalPrice = priceService.calculatePrice(reservations);

        assertThat(finalPrice).isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("무비데이 할인이 먼저 적용되고, 이어서 시간대 할인이 적용된다.")
    void calculatePrice_applies_both_discounts_in_correct_order() {
        // given
        testScreening = new Screening(testMovie, null, LocalDateTime.of(2025, 10, 10, 9, 30)); // 무비데이 & 시간대
        Seat sSeat1 = new Seat('P', 1, SeatGrade.S, false);
        Seat sSeat2 = new Seat('P', 2, SeatGrade.S, false);
        reservations = List.of(new Reservation(testScreening, List.of(sSeat1, sSeat2)));

        // 1. 무비데이 할인 적용: (18000 * 2) * 0.9 = 32400
        // 2. 시간대 할인 적용: 32400 - 2000 = 30400
        int expectedPrice = 30400;
        int finalPrice = priceService.calculatePrice(reservations);

        assertThat(finalPrice).isEqualTo(expectedPrice);
    }
}