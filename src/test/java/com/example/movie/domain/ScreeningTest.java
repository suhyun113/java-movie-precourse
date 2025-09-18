package com.example.movie.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ScreeningTest {
    @Test
    @DisplayName("영화 상영 종료 시간을 올바르게 계산한다.")
    void getEndDateTime_calculate_correctly() {
        // given
        LocalDate releaseDate = LocalDate.of(2025, 9, 10);
        LocalDate finishDate = LocalDate.of(2025, 9, 30);
        Movie movie = new Movie("F1 더무비", Duration.ofMinutes(120), releaseDate, finishDate);
        Theater theater = new Theater(1, 9, 23, 16, 20);
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 10, 15, 0);
        Screening screening = new Screening(movie, theater, startTime);

        // when
        LocalDateTime endDateTime = screening.getEndDateTime();

        // then
        assertThat(endDateTime).isEqualTo(LocalDateTime.of(2025, 1, 10, 17, 0));
    }

    @Test
    @DisplayName("유효한 상영 스케줄이면 true를 반환한다.")
    void isValidSchedule_returns_true() {
        // given
        LocalDate releaseDate = LocalDate.of(2025, 9, 10);
        LocalDate finishDate = LocalDate.of(2025, 9, 30);
        Movie movie = new Movie("F1 더무비", Duration.ofMinutes(120), releaseDate, finishDate);
        Theater theater = new Theater(1, 9, 23, 16, 20);
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 20, 10, 0);
        Screening screening = new Screening(movie, theater, startTime);

        // then
        assertThat(screening.isValidSchedule()).isTrue();
    }

    @Test
    @DisplayName("영화 상영 기간을 벗어나면 유효하지 않은 스케줄이다.")
    void isValidSchedule_invalid_movie_date_returns_false() {
        // given
        LocalDate releaseDate = LocalDate.of(2025, 9, 10);
        LocalDate finishDate = LocalDate.of(2025, 9, 30);
        Movie movie = new Movie("F1 더무비", Duration.ofMinutes(120), releaseDate, finishDate);
        Theater theater = new Theater(1, 9, 23, 16, 20);
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 9, 10, 0); // 개봉일 이전
        Screening screening = new Screening(movie, theater, startTime);

        // then
        assertThat(screening.isValidSchedule()).isFalse();
    }

    @Test
    @DisplayName("상영관 운영 시간을 벗어나면 유효하지 않은 스케줄이다.")
    void isValidSchedule_invalid_theater_hours_returns_false() {
        // given
        LocalDate releaseDate = LocalDate.of(2025, 9, 10);
        LocalDate finishDate = LocalDate.of(2025, 9, 30);
        Movie movie = new Movie("F1 더무비", Duration.ofMinutes(120), releaseDate, finishDate);
        Theater theater = new Theater(1, 9, 23, 16, 20);
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 20, 22, 0); // ends at 23:30 (outside hours)
        Screening screening = new Screening(movie, theater, startTime);

        // then
        assertThat(screening.isValidSchedule()).isFalse();
    }
}