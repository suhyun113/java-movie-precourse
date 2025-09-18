package com.example.movie.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CustomerTest {
    @Test
    @DisplayName("보유 포인트가 올바르게 차감된다.")
    void usePoints_success() {
        Customer customer = new Customer("testUser1", "테스트사용자1", 5000);
        customer.usePoints(3000);
        assertThat(customer.getPoints()).isEqualTo(2000);
    }

    @Test
    @DisplayName("보유 포인트가 부족하면 예외를 발생시킨다.")
    void usePoints_fail() {
        Customer customer = new Customer("testUser1", "테스트사용자1", 5000);
        assertThatThrownBy(() -> customer.usePoints(6000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("로 보유 포인트가 부족합니다.");
    }
}
