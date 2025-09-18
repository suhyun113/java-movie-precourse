package com.example.movie.customer;

public class Customer {
    private final String userId;
    private final String userName;
    private final int points;

    public Customer(String userId, String userName, int points) {
        this.userId = userId;
        this.userName = userName;
        this.points = points;
    }

    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public int getPoints() { return points; }
}
