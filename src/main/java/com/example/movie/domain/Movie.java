package com.example.movie.domain;

import java.time.Duration;
import java.time.LocalDate;

public class Movie {
    private final String title;
    private final Duration runningTime; // 분
    private final LocalDate releaseDate; // 개봉일
    private final LocalDate finishDate; // 종영일

    public Movie(String title, Duration runningTime, LocalDate releaseDate, LocalDate finishDate) {
        this.title = title;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
        this.finishDate = finishDate;
    }

    public String getTitle() { return title; }
    public Duration getRunningTime() { return runningTime; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public LocalDate getFinishDate() { return finishDate; }

    // 영화 상영 가능 기간 (개봉일 ~ 종영일)
    public boolean isShowing(LocalDate date) {
        return !date.isBefore(releaseDate) && !date.isAfter(finishDate);
    }
}
