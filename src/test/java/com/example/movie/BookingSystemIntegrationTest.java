package com.example.movie;

import com.example.movie.domain.*;
import com.example.movie.enums.PaymentType;
import com.example.movie.enums.SeatGrade;
import com.example.movie.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("예매 시스템 통합 테스트")
class BookingSystemIntegrationTest {

    private ScreeningService screeningService;
    private BookingService bookingService;
    private PriceService priceService;
    private PaymentService paymentService;

    private Customer customer;
    private Movie movie;
    private Theater theater;

    @BeforeEach
    void setUp() {
        // 서비스 객체들 초기화
        screeningService = new ScreeningService();
        bookingService = new BookingService();
        priceService = new PriceService();
        paymentService = new PaymentService();

        // 테스트에 필요한 도메인 객체 생성
        customer = new Customer("user1", "통합테스터", 10000);
        movie = new Movie("통합 테스트 영화", Duration.ofMinutes(120), LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 31));
        theater = new Theater(1, 9, 25, 16, 20);
    }

    @Test
    @DisplayName("전체 예매 및 결제 프로세스가 성공적으로 완료되어야 한다.")
    void fullBookingAndPaymentProcess_success() {
        // given
        // 상영 등록 (무비데이 & 시간대 할인 조건)
        Screening screening = new Screening(movie, theater, LocalDateTime.of(2025, 10, 20, 10, 0));
        screeningService.addScreening(screening);

        // B등급 좌석 2개 선택
        Seat seat1 = new Seat('A', 1, SeatGrade.B, false);
        Seat seat2 = new Seat('A', 2, SeatGrade.B, false);
        Reservation reservation = new Reservation(screening, List.of(seat1, seat2));
        List<Reservation> reservations = List.of(reservation);

        int pointsToUse = 500;
        PaymentType paymentType = PaymentType.CREDIT_CARD;

        // when
        // 1. 예매 시도 (BookingService)
        bookingService.createBooking(customer, reservations);

        // 2. 가격 계산 (PriceService)
        int calculatedPrice = priceService.calculatePrice(reservations);

        // 3. 결제 시도 (PaymentService)
        int finalPaymentPrice = paymentService.processPayment(customer, reservations, pointsToUse, paymentType);

        // then (결과 검증)
        assertThat(seat1.isReserved()).isTrue();
        assertThat(seat2.isReserved()).isTrue();

        // 가격 계산 검증
        // 기본 가격: 12000원 * 2 = 24000원
        // 무비데이 할인: 24000원 * 0.1 = 2400원
        // 시간대 할인: 24000원 - 2400원 = 21600원 - 2000원 = 19600원
        assertThat(calculatedPrice).isEqualTo(19600);

        // 최종 결제 금액 검증
        // 포인트 적용: 19600원 - 500포인트 = 19100원
        // 카드 할인: 19100원 * 0.95 = 18145원
        assertThat(finalPaymentPrice).isEqualTo(18145);

        // 고객 포인트 차감 검증
        assertThat(customer.getPoints()).isEqualTo(10000 - pointsToUse);
    }

    @Test
    @DisplayName("시간이 겹치는 예매를 시도하면 예외가 발생하고, 좌석은 예약되지 않아야 한다.")
    void bookingWithTimeConflict_throwsExceptionAndSeatsAreNotReserved() {
        // given
        // 필요한 상영만 테스트 케이스 내에서 직접 등록
        Screening screening1 = new Screening(movie, theater, LocalDateTime.of(2025, 10, 20, 10, 0));
        screeningService.addScreening(screening1);

        // 다른 상영관에서 시간이 겹치는 상영
        Screening screening2 = new Screening(movie, new Theater(2, 9, 25, 10, 10), LocalDateTime.of(2025, 10, 20, 11, 0));

        Seat seat1 = new Seat('A', 1, SeatGrade.B, false);
        Seat seat2 = new Seat('B', 1, SeatGrade.B, false);
        Reservation reservation1 = new Reservation(screening1, List.of(seat1));
        Reservation reservation2 = new Reservation(screening2, List.of(seat2));
        List<Reservation> reservations = List.of(reservation1, reservation2);

        // when & then
        assertThatThrownBy(() -> bookingService.createBooking(customer, reservations))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("시간이 겹치는 상영은 함께 예매할 수 없습니다.");

        // 예외 발생 시 좌석은 예약되지 않았음을 검증
        assertThat(seat1.isReserved()).isFalse();
        assertThat(seat2.isReserved()).isFalse();
    }
}