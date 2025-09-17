package com.example.movie.movie;

public class Movie {
    private final String title;
    private final int runningTime; // 분

    public Movie(String title, int runningTime) {
        this.title = title;
        this.runningTime = runningTime;
    }

    public String getTitle() { return title; }
    public int getRunningTime() { return runningTime; }
}
