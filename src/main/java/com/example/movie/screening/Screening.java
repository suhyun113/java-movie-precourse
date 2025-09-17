package com.example.movie.screening;

import com.example.movie.movie.Movie;
import com.example.movie.theater.Theater;

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

    // 상영 종료 시간 계산
    public LocalDateTime getEndDateTime() {
        return startDateTime.plusMinutes(movie.getRunningTime());
    }
}