package com.example.movie.policy;

import com.example.movie.domain.Screening;

public interface DiscountPolicy {
        int calculateDiscountAmount(Screening screening, int price);
}
