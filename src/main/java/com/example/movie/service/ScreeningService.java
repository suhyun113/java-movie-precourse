package com.example.movie.service;

import com.example.movie.domain.Screening;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScreeningService {
    private final List<Screening> allScreenings = new ArrayList<>();

    // 상영 추가
    public void addScreening(Screening newScreening) {
        // 상영이 가능한가
        if (!newScreening.isValidSchedule()) {
            throw new IllegalArgumentException("영화 상영 스케줄이 영화관 운영 시간 또는 영화 상영 기간을 벗어납니다.");
        }

        // 같은 영화관의 상영
        List<Screening> screeningsInSameTheater = allScreenings.stream()
                .filter(s -> s.getTheater().getNumber() == newScreening.getTheater().getNumber())
                .collect(Collectors.toList());

        boolean hasTimeConflict = screeningsInSameTheater.stream()
                .anyMatch(s -> s.isTimeConflict(newScreening));

        if (hasTimeConflict) {
            throw new IllegalStateException("해당 상영관에 시간이 겹치는 상영이 이미 존재합니다.");
        }

        allScreenings.add(newScreening);
    }

    public List<Screening> getAllScreenings() {
        return allScreenings;
    }
}
