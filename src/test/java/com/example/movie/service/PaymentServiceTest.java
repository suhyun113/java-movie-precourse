package com.example.movie.service;

import com.example.movie.domain.Customer;
import com.example.movie.domain.Movie;
import com.example.movie.domain.Reservation;
import com.example.movie.domain.Screening;
import com.example.movie.domain.Seat;
import com.example.movie.enums.PaymentType;
import com.example.movie.enums.SeatGrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentServiceTest {

    private PaymentService paymentService;
    private Customer customer;
    private List<Reservation> reservations;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService();
        customer = new Customer("testUser1", "테스트사용자1", 5000);
        LocalDate releaseDate = LocalDate.of(2025, 9, 10);
        LocalDate finishDate = LocalDate.of(2025, 9, 30);
        Movie movie = new Movie("F1 더무비", Duration.ofMinutes(120), releaseDate, finishDate);

        Screening screening = new Screening(movie, null, LocalDateTime.of(2025, 9, 15, 15, 0));
        Seat seat = new Seat('A', 1, SeatGrade.B, false);
        reservations = List.of(new Reservation(screening, List.of(seat)));
    }

    @Test
    @DisplayName("포인트와 신용카드 할인이 올바르게 적용된다.")
    void processPayment_applies_points_and_creditCard_discount() {
        // 기본 가격은 SeatGrade.B의 가격(12000원)을 사용
        int basePrice = SeatGrade.B.getPrice();
        int pointsToUse = 1000;
        int expectedPrice = (int)((basePrice - pointsToUse) * (1 - PaymentType.CREDIT_CARD.getDiscountRate()));

        int finalPrice = paymentService.processPayment(customer, reservations, pointsToUse, PaymentType.CREDIT_CARD);

        assertThat(finalPrice).isEqualTo(expectedPrice);
        // 고객의 초기 포인트가 5000원이므로, 5000원에서 1000원을 차감한 값으로 검증
        assertThat(customer.getPoints()).isEqualTo(5000 - pointsToUse);
    }

    @Test
    @DisplayName("포인트와 현금 할인이 올바르게 적용된다.")
    void processPayment_applies_points_and_cash_discount() {
        int basePrice = SeatGrade.B.getPrice();
        int pointsToUse = 500;
        int expectedPrice = (int)((basePrice - pointsToUse) * (1 - PaymentType.CASH.getDiscountRate()));
        int finalPrice = paymentService.processPayment(customer, reservations, pointsToUse, PaymentType.CASH);

        assertThat(finalPrice).isEqualTo(expectedPrice);
        assertThat(customer.getPoints()).isEqualTo(5000 - pointsToUse);
    }

    @Test
    @DisplayName("보유 포인트가 부족하면 예외를 발생시킨다.")
    void processPayment_insufficient_points_throws_exception() {
        // 고객의 보유 포인트(5000원)보다 많은 포인트 사용
        int pointsToUse = 6000;

        assertThatThrownBy(() -> paymentService.processPayment(customer, reservations, pointsToUse, PaymentType.CREDIT_CARD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("보유 포인트가 부족합니다.");
    }
}