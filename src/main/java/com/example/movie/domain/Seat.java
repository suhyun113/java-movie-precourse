package com.example.movie.domain;

import com.example.movie.enums.SeatGrade;

public class Seat {
    private final char row;
    private final int column; // 반드시 존재
    private final SeatGrade grade;
    private boolean isReserved;

    public Seat(char row, int column, SeatGrade grade, boolean isReserved) {
        this.row = row;
        this.column = column;
        this.grade = grade;
        this.isReserved = isReserved;
    }

    public char getRow() { return row; }
    public int getColumn() { return column; }
    public SeatGrade getGrade() { return grade; }
    public boolean isReserved() { return isReserved; }

    // 좌석 코드 (ex: A1, C3, E4)
    public String getSeatCode() {
        return row + String.valueOf(column);
    }

    // 좌석 예약 여부
    public void reserveSeat() {
        if (isReserved) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }
        this.isReserved = true;
    }
}
