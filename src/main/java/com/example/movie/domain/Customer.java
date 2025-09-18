package com.example.movie.domain;

public class Customer {
    private final String userId;
    private final String userName;
    private int points;

    public Customer(String userId, String userName, int points) {
        this.userId = userId;
        this.userName = userName;
        this.points = points;
    }

    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public int getPoints() { return points; }

    public void usePoints(int pointsToUse) {
        if (this.points < pointsToUse) {
            throw new IllegalArgumentException(pointsToUse + "로 보유 포인트가 부족합니다.");
        }
        this.points -= pointsToUse;
    }
}
