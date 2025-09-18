package com.example.movie.domain;

import java.time.LocalDateTime;

public class Screening {
    private final Movie movie;
    private final Theater theater;
    private final LocalDateTime startDateTime;

    public Screening(Movie movie, Theater theater, LocalDateTime startDateTime) {
        this.movie = movie;
        this.theater = theater;
        this.startDateTime = startDateTime;
    }

    public Movie getMovie() { return movie; }
    public Theater getTheater() { return theater; }
    public LocalDateTime getStartDateTime() { return startDateTime; }

    // 상영 종료 시간 계산 (영화 시작 시간 + 영화 러닝 타임)
    public LocalDateTime getEndDateTime() {
        return startDateTime.plus(movie.getRunningTime());
    }

    // 상영 가능한지 확인
    public boolean isValidSchedule() {
        boolean isMovieAvailable = movie.isShowing(startDateTime.toLocalDate()); // 날짜만 비교
        boolean isTheaterOperating = theater.isOperating(startDateTime, getEndDateTime());
        return isMovieAvailable && isTheaterOperating;
    }
}