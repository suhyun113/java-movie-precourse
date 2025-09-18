package com.example.movie.service;

import com.example.movie.domain.Movie;
import com.example.movie.domain.Screening;
import com.example.movie.domain.Theater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScreeningServiceTest {

    private ScreeningService screeningService;
    private Movie validMovie;
    private Theater validTheater;

    @BeforeEach
    void setUp() {
        screeningService = new ScreeningService();
        LocalDate releaseDate = LocalDate.of(2025, 9, 10);
        LocalDate finishDate = LocalDate.of(2025, 9, 30);
        validMovie = new Movie("F1 더무비", Duration.ofMinutes(120), releaseDate, finishDate);
        validTheater = new Theater(1, 9, 25, 16, 20);
    }

    @Test
    @DisplayName("유효한 상영 스케줄을 추가할 수 있다.")
    void addScreening_withValidSchedule_success() {
        Screening newScreening = new Screening(validMovie, validTheater, LocalDateTime.of(2025, 9, 15, 15, 0));
        screeningService.addScreening(newScreening);

        assertThat(screeningService.getAllScreenings()).hasSize(1);
    }

    @Test
    @DisplayName("영화관 운영 시간을 벗어나는 상영은 추가할 수 없다.")
    void addScreening_withInvalidTheaterTime_throwsException() {
        // 영화관 운영 시작 시간(9시) 이전
        Screening invalidScreening = new Screening(validMovie, validTheater, LocalDateTime.of(2025, 9, 15, 8, 0));

        assertThatThrownBy(() -> screeningService.addScreening(invalidScreening))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("영화 상영 스케줄이 영화관 운영 시간");
    }

    @Test
    @DisplayName("동일 상영관에 시간이 겹치는 상영은 추가할 수 없다.")
    void addScreening_withTimeConflict_throwsException() {
        // 영화의 상영 가능 기간(9월 10일 ~ 9월 30일) 내의 날짜 사용
        Screening existingScreening = new Screening(validMovie, validTheater, LocalDateTime.of(2025, 9, 15, 15, 0));
        screeningService.addScreening(existingScreening);
        // 시간이 겹치는 새로운 상영 (1시간 뒤 시작)
        Screening conflictingScreening = new Screening(validMovie, validTheater, LocalDateTime.of(2025, 9, 15, 16, 0));

        assertThatThrownBy(() -> screeningService.addScreening(conflictingScreening))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("시간이 겹치는 상영이 이미 존재합니다");
    }

    @Test
    @DisplayName("다른 상영관에 시간이 겹치는 상영은 추가할 수 있다.")
    void addScreening_withConflictInDifferentTheater_success() {
        Theater otherTheater = new Theater(2, 9, 25, 16, 20);
        Screening existingScreening = new Screening(validMovie, validTheater, LocalDateTime.of(2025, 9, 15, 15, 0));
        screeningService.addScreening(existingScreening);
        // 다른 상영관에서 시간이 겹치는 상영
        Screening nonConflictingScreening = new Screening(validMovie, otherTheater, LocalDateTime.of(2025, 9, 15, 15, 0));
        screeningService.addScreening(nonConflictingScreening);

        assertThat(screeningService.getAllScreenings()).hasSize(2);
    }
}