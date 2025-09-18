package com.example.movie.service;

import com.example.movie.domain.Customer;
import com.example.movie.domain.Reservation;
import com.example.movie.enums.PaymentType;
import java.util.List;

public class PaymentService {
    private final PriceService priceService = new PriceService();

    public int processPayment(Customer customer, List<Reservation> reservations, int pointsToUse, PaymentType paymentType) {
        int totalBookingPrice = priceService.calculatePrice(reservations);

        // 1. 포인트 적용
        if (customer.getPoints() < pointsToUse) {
            throw new IllegalArgumentException("보유 포인트가 부족합니다.");
        }
        int priceAfterPoints = totalBookingPrice - pointsToUse;
        customer.usePoints(pointsToUse);

        // 2. 결제 수단 할인 적용
        return (int) (priceAfterPoints * (1 - paymentType.getDiscountRate()));
    }
}