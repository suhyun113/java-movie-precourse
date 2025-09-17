package com.example.movie.theater;

import java.time.LocalTime;

public class Theater {
    private final String name;
    private final LocalTime openTime;
    private final LocalTime closeTime;

    public Theater(String name, LocalTime openTime, LocalTime closeTime) {
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public String getName() { return name; }
    public LocalTime getOpenTime() { return openTime; }
    public LocalTime getCloseTime() { return closeTime; }
}
