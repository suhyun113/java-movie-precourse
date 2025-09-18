package com.example.movie.reservation;

import com.example.movie.customer.Customer;
import com.example.movie.screening.Screening;
import com.example.movie.seat.Seat;

import java.util.List;

public class Reservation {
    private final Customer customer;
    private final Screening screening;
    private final List<Seat> seats;

    public Reservation(Customer customer, Screening screening, List<Seat> seats) {
        this.customer = customer;
        this.screening = screening;
        this.seats = seats;
    }

    public Customer getCustomer() { return customer; }
    public Screening getScreening() { return screening; }
    public List<Seat> getSeats() { return seats; }
}
