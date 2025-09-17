package com.example.movie;

public class Seat {
    private final char row;
    private final int column; // 반드시 존재
    private final char grade;

    public Seat(char row, int column, char grade) {
        this.row = row;
        this.column = column;
        this.grade = grade;
    }

    public char getRow() { return row; }
    public int getColumn() { return column; }
    public char getGrade() { return grade; }

    // 좌석 코드 (ex: A1, C3, E4)
    public String getSeatCode() {
        return row + String.valueOf(column);
    }
}
