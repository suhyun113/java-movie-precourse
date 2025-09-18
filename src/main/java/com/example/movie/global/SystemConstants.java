package com.example.movie.global;

public class SystemConstants {
    private SystemConstants() {} // 인스턴스화 방지

    // 무비데이 할인 날짜 (매월 10일, 20일, 30일)
    public static final int MOVIE_DAY_DATE_1 = 10;
    public static final int MOVIE_DAY_DATE_2 = 20;
    public static final int MOVIE_DAY_DATE_3 = 30;
    public static final double MOVIE_DAY_DISCOUNT_RATE = 0.1;

    // 시간대 할인 조건 (오전 11시 이전 또는 오후 8시 이후)
    public static final int TIME_SLOT_HOUR_BEFORE = 11;
    public static final int TIME_SLOT_HOUR_AFTER = 20;
    public static final int TIME_SLOT_DISCOUNT_AMOUNT = 2000;
}
