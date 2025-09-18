package com.example.movie.enums;

public enum SeatGrade {
    S(18000),
    A(15000),
    B(12000);

    private final int price;

    SeatGrade(int price) {
        this.price = price;
    }

    public int getPrice() {return price;}
}
