package com.example.movie.domain;

import com.example.movie.enums.SeatGrade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SeatTest {

    @Test
    @DisplayName("좌석 코드를 올바르게 생성한다.")
    void getSeatCode_generates_correct_code() {
        Seat seat = new Seat('A', 1, SeatGrade.B, false);
        String seatCode = seat.getSeatCode();

        assertThat(seatCode).isEqualTo("A1");
    }

    @Test
    @DisplayName("좌석을 예약하면 예약 상태가 true가 된다.")
    void reserveSeat_sets_isReserved_to_true() {
        Seat seat = new Seat('B', 5, SeatGrade.B, false);
        seat.reserveSeat();

        assertThat(seat.isReserved()).isTrue();
    }

    @Test
    @DisplayName("이미 예약된 좌석을 다시 예약하려고 하면 예외를 발생시킨다.")
    void reserveSeat_already_reserved_throws_exception() {
        Seat seat = new Seat('C', 3, SeatGrade.B, true);

        assertThatThrownBy(seat::reserveSeat)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 예약된 좌석입니다.");
    }

    @Test
    @DisplayName("좌석 등급을 올바르게 반환한다.")
    void getGrade_returns_correct_grade() {
        Seat seat = new Seat('A', 1, SeatGrade.B, false);
        SeatGrade grade = seat.getGrade();

        assertThat(grade).isEqualTo(SeatGrade.B);
    }
}