package com.example.movie.domain;

import java.util.List;

public class Reservation {
    private final Screening screening;
    private final List<Seat> seats;

    public Reservation(Screening screening, List<Seat> seats) {
        this.screening = screening;
        this.seats = seats;
    }

    public Screening getScreening() { return screening; }
    public List<Seat> getSeats() { return seats; }
}
