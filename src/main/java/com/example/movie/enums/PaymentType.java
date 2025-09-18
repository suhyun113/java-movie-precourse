package com.example.movie.enums;

public enum PaymentType {
    CREDIT_CARD(0.05),
    CASH(0.02);

    private final double discountRate;

    PaymentType(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {return discountRate;}
}
