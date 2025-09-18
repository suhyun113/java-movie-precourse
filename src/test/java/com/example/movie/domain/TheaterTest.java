package com.example.movie.domain;

import com.example.movie.enums.SeatGrade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TheaterTest {

    @Test
    @DisplayName("영화 시작과 종료 시간이 운영 시간 내에 있으면 true를 반환한다.")
    void isOperating_withinHours_returnsTrue() {
        // 09:00 ~ 23:00 운영하는 영화관 (좌석 16행 20열 생성)
        Theater theater = new Theater(1, 9, 23, 16, 20);
        // 영화 상영 시간: 10:00 to 12:00
        LocalDateTime startTime = LocalDateTime.of(2025, 10, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 10, 20, 12, 0);

        assertThat(theater.isOperating(startTime, endTime)).isTrue();
    }

    @Test
    @DisplayName("영화 종료 시간이 운영 시간 밖이면 false를 반환한다.")
    void isOperating_endTimeOutsideHours_returnsFalse() {
        // 09:00 ~ 23:00 운영하는 영화관 (좌석 16행 20열 생성)
        Theater theater = new Theater(1, 9, 23, 16, 20);
        // 영화 상영 시간: 22:30 to 23:30 (ends after 23:00)
        LocalDateTime startTime = LocalDateTime.of(2025, 10, 20, 22, 30);
        LocalDateTime endTime = LocalDateTime.of(2025, 10, 20, 23, 30);

        assertThat(theater.isOperating(startTime, endTime)).isFalse();
    }

    @Test
    @DisplayName("운영 시간이 자정을 넘는 경우, 영화 상영 시간이 모두 운영 시간 내에 있으면 true를 반환한다.")
    void isOperating_acrossMidnight_withinHours_returnsTrue() {
        // 23:00 ~ 25:00 (다음날 01:00) 운영하는 영화관 (좌석 16행 20열 생성)
        Theater theater = new Theater(1, 23, 25, 16, 20);
        // 영화 상영 시간: 23:30 to 00:30 (다음날)
        LocalDateTime startTime = LocalDateTime.of(2025, 10, 20, 23, 30);
        LocalDateTime endTime = LocalDateTime.of(2025, 10, 21, 0, 30);

        assertThat(theater.isOperating(startTime, endTime)).isTrue();
    }

    @Test
    @DisplayName("운영 시간이 자정을 넘는 경우에도, 종료 시간이 운영 시간 밖이면 false를 반환한다.")
    void isOperating_acrossMidnight_endTimeOutsideHours_returnsFalse() {
        // 23:00 ~ 25:00 (다음날 01:00) 운영하는 영화관 (좌석 16행 20열 생성)
        Theater theater = new Theater(1, 23, 25, 16, 20);
        // 영화 상영 시간: 00:30 to 01:30 (다음날)
        LocalDateTime startTime = LocalDateTime.of(2025, 10, 21, 0, 30);
        LocalDateTime endTime = LocalDateTime.of(2025, 10, 21, 1, 30); // 25:30

        assertThat(theater.isOperating(startTime, endTime)).isFalse();
    }

    @Test
    @DisplayName("좌석 등급이 행에 따라 올바르게 할당되는지 확인한다.")
    void createSeats_assigns_correct_grade_based_on_row() {
        // 16행 20열의 영화관을 생성하여 좌석 등급 할당 로직을 실행
        Theater theater = new Theater(1, 9, 23, 16, 20);

        // A1 좌석을 찾아서 등급을 확인
        Seat seatA1 = theater.getSeats().stream()
                .filter(seat -> seat.getSeatCode().equals("A1"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("좌석이 존재하지 않습니다."));

        // A열 좌석은 B 등급이어야 함
        assertThat(seatA1.getGrade()).isEqualTo(SeatGrade.B);
    }
}