package com.example.movie.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Theater {
    private final int number;
    private final int openingHour;
    private final int closingHour;
    private final List<Seat> seats;

    public Theater(int number, int openingHour, int closingHour, List<Seat> seats) {
        this.number = number;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.seats = seats;
    }

    public int getNumber() {return number;}
    public int getOpeningHour() {return openingHour;}
    public int getClosingHour() {return closingHour;}
    public List<Seat> getSeats() {return seats;}


    // 영화관 운영 시간
    public boolean isOperating(LocalDateTime startTime, LocalDateTime endTime) {
        return isTimeWithinOperatingHours(startTime) && isTimeWithinOperatingHours(endTime);
    }

    // 영화관 운영 시간 내 영화 상영 시간 확인
    public boolean isTimeWithinOperatingHours(LocalDateTime time) {
        int currentHour = time.getHour();
        int currentMinute = time.getMinute();

        if (currentHour < openingHour && closingHour >= 24) {
            currentHour += 24;
        }

        int currentTotalMinutes = currentHour * 60 + currentMinute;
        int openingTotalMinutes = openingHour * 60;
        int closingTotalMinutes = closingHour * 60;

        return currentTotalMinutes >= openingTotalMinutes && currentTotalMinutes <= closingTotalMinutes;
    }
}
