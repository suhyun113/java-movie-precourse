package com.example.movie.service;

import com.example.movie.domain.Customer;
import com.example.movie.domain.Reservation;
import com.example.movie.domain.Screening;
import com.example.movie.domain.Seat;
import java.util.List;

public class BookingService {
    public void createBooking(Customer customer, List<Reservation> reservations) {
        validateReservations(reservations);

        for (Reservation reservation : reservations) {
            for (Seat seat : reservation.getSeats()) {
                seat.reserveSeat();
            }
        }
    }

    private void validateReservations(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            throw new IllegalArgumentException("예약할 상영 및 좌석 정보가 없습니다.");
        }

        validateNoTimeConflicts(reservations);
        validateSeatsAreNotReserved(reservations);
    }

    // 예매하려는 영화들 간의 상영 시간 충돌을 확인
    private void validateNoTimeConflicts(List<Reservation> reservations) {
        for (int i = 0; i < reservations.size(); i++) {
            for (int j = i + 1; j < reservations.size(); j++) {
                Screening s1 = reservations.get(i).getScreening();
                Screening s2 = reservations.get(j).getScreening();

                if (s1.isTimeConflict(s2)) {
                    throw new IllegalArgumentException("시간이 겹치는 상영은 함께 예매할 수 없습니다.");
                }
            }
        }
    }

    // 선택한 좌석이 이미 예약된 상태인지 확인
    private void validateSeatsAreNotReserved(List<Reservation> reservations) {
        for (Reservation reservation : reservations) {
            for (Seat seat : reservation.getSeats()) {
                if (seat.isReserved()) {
                    throw new IllegalStateException("선택한 좌석 중 이미 예약된 좌석이 있습니다.");
                }
            }
        }
    }
}