package com.kakaopay.coupon.api.persistence.repository;

import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CouponRepositoryTest {
    @Autowired
    private CouponRepository couponRepository;
    private CouponEntity savedCouponEntity;


    @BeforeEach
    void setUp() throws Exception {
        savedCouponEntity =
                CouponEntity.builder()
                        .no(0L)
                        .code("test-coupon")
                        .build();
        couponRepository.save(savedCouponEntity);
    }

    @Test
    void 존재하는_코드로_쿠폰_조회() {
        CouponEntity couponEntity = couponRepository.findByCode("test-coupon").orElseThrow();

        assertEquals(savedCouponEntity.getCode(), couponEntity.getCode());
    }

    @Test
    void 존재하지_않는_코드로_쿠폰_조회() {
        assertThrows(RuntimeException.class, () -> {
            CouponEntity couponEntity = couponRepository.findByCode("test-coupon1").orElseThrow();
        });
    }
}