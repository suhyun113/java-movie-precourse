package com.example.movie.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;

class TheaterTest {

    @Test
    @DisplayName("영화 시작과 종료 시간이 운영 시간 내에 있으면 true를 반환한다.")
    void isOperating_withinHours_returnsTrue() {
        // 09:00 ~ 23:00 운영하는 영화관
        Theater theater = new Theater(1, 9, 23, Collections.emptyList());
        // 10:00 to 12:00 영화 상영 시간
        LocalDateTime startTime = LocalDateTime.of(2025, 10, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 10, 20, 12, 0);

        assertThat(theater.isOperating(startTime, endTime)).isTrue();
    }

    @Test
    @DisplayName("영화 종료 시간이 운영 시간 밖이면 false를 반환한다.")
    void isOperating_endTimeOutsideHours_returnsFalse() {
        // 09:00 ~ 23:00 운영하는 영화관
        Theater theater = new Theater(1, 9, 23, Collections.emptyList());
        // 22:30 to 23:30 (23:00 이후) 영화 상영 시간
        LocalDateTime startTime = LocalDateTime.of(2025, 10, 20, 22, 30);
        LocalDateTime endTime = LocalDateTime.of(2025, 10, 20, 23, 30);

        assertThat(theater.isOperating(startTime, endTime)).isFalse();
    }

    @Test
    @DisplayName("운영 시간이 자정을 넘는 경우, 영화 상영 시간이 모두 운영 시간 내에 있으면 true를 반환한다.")
    void isOperating_acrossMidnight_withinHours_returnsTrue() {
        // 23:00 ~ 25:00 (다음날 01:00) 운영하는 영화관
        Theater theater = new Theater(1, 23, 25, Collections.emptyList());
        // 23:30 to 00:30 (다음날) 영화 상영 시간
        LocalDateTime startTime = LocalDateTime.of(2025, 10, 20, 23, 30);
        LocalDateTime endTime = LocalDateTime.of(2025, 10, 21, 0, 30);

        assertThat(theater.isOperating(startTime, endTime)).isTrue();
    }

    @Test
    @DisplayName("운영 시간이 자정을 넘는 경우에도, 종료 시간이 운영 시간 밖이면 false를 반환한다.")
    void isOperating_acrossMidnight_endTimeOutsideHours_returnsFalse() {
        // 23:00 ~ 25:00 (다음날 01:00) 운영하는 영화관
        Theater theater = new Theater(1, 23, 25, Collections.emptyList());
        // 00:30 to 01:30 (다음날) 영화 상영 시간
        LocalDateTime startTime = LocalDateTime.of(2025, 10, 21, 0, 30);
        LocalDateTime endTime = LocalDateTime.of(2025, 10, 21, 1, 30); // 25:30

        assertThat(theater.isOperating(startTime, endTime)).isFalse();
    }
}