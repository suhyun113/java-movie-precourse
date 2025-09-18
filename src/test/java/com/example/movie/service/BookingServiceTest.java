package com.example.movie.service;

import com.example.movie.domain.Customer;
import com.example.movie.domain.Movie;
import com.example.movie.domain.Reservation;
import com.example.movie.domain.Screening;
import com.example.movie.domain.Seat;
import com.example.movie.domain.Theater;
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

class BookingServiceTest {

    private BookingService bookingService;
    private Customer customer;
    private Movie movie;
    private Theater theater1;
    private Theater theater2;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService();
        customer = new Customer("testUser1", "테스트사용자1", 5000);
        LocalDate releaseDate = LocalDate.of(2025, 9, 10);
        LocalDate finishDate = LocalDate.of(2025, 9, 30);
        movie = new Movie("F1 더무비", Duration.ofMinutes(120), releaseDate, finishDate);

        theater1 = new Theater(1, 9, 25, 10, 10);
        theater2 = new Theater(2, 9, 25, 10, 10);
    }

    @Test
    @DisplayName("여러 영화를 성공적으로 예매할 수 있다.")
    void createBooking_withMultipleReservations_success() {
        // 다른 영화관, 같은 영화, 시간이 겹치지 않음
        Screening screening1 = new Screening(movie, theater1, LocalDateTime.of(2025, 9, 20, 10, 0));
        Screening screening2 = new Screening(movie, theater2, LocalDateTime.of(2025, 9, 20, 14, 0));
        Seat seat1 = new Seat('A', 1, SeatGrade.B, false);
        Seat seat2 = new Seat('B', 1, SeatGrade.B, false);

        Reservation reservation1 = new Reservation(screening1, List.of(seat1));
        Reservation reservation2 = new Reservation(screening2, List.of(seat2));
        List<Reservation> reservations = List.of(reservation1, reservation2);
        bookingService.createBooking(customer, reservations);

        assertThat(seat1.isReserved()).isTrue();
        assertThat(seat2.isReserved()).isTrue();
    }

    @Test
    @DisplayName("시간이 겹치는 상영은 함께 예매할 수 없다.")
    void createBooking_withTimeConflict_throwsException() {
        // 다른 영화관, 같은 영화, 시간이 겹침
        Screening screening1 = new Screening(movie, theater1, LocalDateTime.of(2025, 9, 20, 10, 0)); // 10:00 ~ 12:00
        Screening screening2 = new Screening(movie, theater2, LocalDateTime.of(2025, 9, 20, 11, 0)); // 11:00 ~ 13:00 (겹침)
        Seat seat1 = new Seat('A', 1, SeatGrade.B, false);
        Seat seat2 = new Seat('B', 1, SeatGrade.B, false);

        Reservation reservation1 = new Reservation(screening1, List.of(seat1));
        Reservation reservation2 = new Reservation(screening2, List.of(seat2));
        List<Reservation> reservations = List.of(reservation1, reservation2);

        assertThatThrownBy(() -> bookingService.createBooking(customer, reservations))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시간이 겹치는 상영은 함께 예매할 수 없습니다.");
    }

    @Test
    @DisplayName("이미 예약된 좌석을 포함하여 예매할 수 없다.")
    void createBooking_withAlreadyReservedSeat_throwsException() {
        Screening screening1 = new Screening(movie, theater1, LocalDateTime.of(2025, 9, 20, 10, 0));
        Seat seat1 = new Seat('A', 1, SeatGrade.B, true); // 이미 예약된 좌석

        Reservation reservation1 = new Reservation(screening1, List.of(seat1));
        List<Reservation> reservations = List.of(reservation1);

        assertThatThrownBy(() -> bookingService.createBooking(customer, reservations))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("선택한 좌석 중 이미 예약된 좌석이 있습니다.");
    }
}