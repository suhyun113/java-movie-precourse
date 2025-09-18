package com.example.movie.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class MovieTest {

    @Test
    @DisplayName("영화가 주어진 기간 내에 상영 가능한지 확인한다.")
    void isShowing_within_date_range() {
        // 준비
        LocalDate releaseDate = LocalDate.of(2025, 9, 10);
        LocalDate finishDate = LocalDate.of(2025, 9, 30);
        Movie movie = new Movie("F1 더무비", Duration.ofMinutes(120), releaseDate, finishDate);

        // 실행 및 검증
        assertThat(movie.isShowing(LocalDate.of(2025, 9, 15))).isTrue(); // 상영 기간 내
        assertThat(movie.isShowing(LocalDate.of(2025, 9, 10))).isTrue(); // 개봉일
        assertThat(movie.isShowing(LocalDate.of(2025, 9, 30))).isTrue(); // 종영일
    }

    @Test
    @DisplayName("영화가 주어진 기간 밖이면 상영 가능하지 않다.")
    void isShowing_outside_date_range() {
        // 준비
        LocalDate releaseDate = LocalDate.of(2025, 9, 10);
        LocalDate finishDate = LocalDate.of(2025, 9, 30);
        Movie movie = new Movie("F1 더무비", Duration.ofMinutes(120), releaseDate, finishDate);

        // 실행 및 검증
        assertThat(movie.isShowing(LocalDate.of(2025, 9, 9))).isFalse(); // 개봉일 이전
        assertThat(movie.isShowing(LocalDate.of(2025, 10, 1))).isFalse(); // 종영일 이후
    }
}
