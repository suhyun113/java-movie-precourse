package com.example.movie.domain;

import com.example.movie.enums.SeatGrade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Theater {
    private final int number;
    private final int openingHour;
    private final int closingHour;
    private final List<Seat> seats;

    public Theater(int number, int openingHour, int closingHour, int totalRows, int toalColumns) {
        this.number = number;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.seats = createSeats(totalRows, toalColumns);
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

    // 좌석 목록 생성 및 등급 할당
    private List<Seat> createSeats(int rows, int columns) {
        List<Seat> newSeats = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            char rowChar = (char) ('A' + i);
            for (int j = 1; j <= columns; j++) {
                SeatGrade grade = calculateSeatGrade(rowChar);
                newSeats.add(new Seat(rowChar, j, grade, false));
            }
        }
        return newSeats;
    }

    // 좌석 등급을 결정
    private SeatGrade calculateSeatGrade(char rowChar) {
        // 총 16줄: A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P
        int rowIndex = rowChar - 'A'; // A: 0
        // 나머지는 A석
        if (rowIndex < 3) { // 맨 앞 3줄
            return SeatGrade.B;
        }
        if (rowIndex >= 10) { // 맨 뒤 4줄 정도
            return SeatGrade.S;
        }
        return SeatGrade.A;
    }
}
