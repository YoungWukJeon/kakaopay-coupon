package com.kakaopay.coupon.api.persistence.repository;

import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CouponRepositoryTest {
    @Autowired
    private CouponRepository couponRepository;
    private CouponEntity savedCouponEntity;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() throws Exception {
        savedCouponEntity =
                CouponEntity.builder()
                        .code("test-coupon")
                        .build();
        testEntityManager.persistAndFlush(savedCouponEntity);
    }

    @Test
    void 존재하는_코드로_쿠폰_조회() {
        CouponEntity couponEntity = couponRepository.findByCode("test-coupon").orElseThrow();
        assertEquals(savedCouponEntity.getCode(), couponEntity.getCode());
    }

    @Test
    void 존재하지_않는_코드로_쿠폰_조회() {
        testEntityManager.remove(savedCouponEntity);
        assertThrows(RuntimeException.class, () -> {
            CouponEntity couponEntity = couponRepository.findByCode("test-coupon").orElseThrow();
        });
    }

    @Test
    void 생성만_된_쿠폰_조회_성공() {
        CouponEntity couponEntity = couponRepository.findByStatus(Status.CREATED).orElseThrow();
        assertEquals(savedCouponEntity.getStatus(), couponEntity.getStatus());
    }

    @Test
    void 생성만_된_쿠폰_조회_실패() {
        testEntityManager.remove(savedCouponEntity);
        assertThrows(RuntimeException.class, () -> {
            CouponEntity couponEntity = couponRepository.findByStatus(Status.CREATED).orElseThrow();
        });
    }

    @Test
    void user에게_할당되지_않은_쿠폰_top1_조회_성공() {
        CouponEntity couponEntity = couponRepository.findByStatus(Status.CREATED).orElseThrow();
        assertEquals(savedCouponEntity.getCode(), couponEntity.getCode());
        assertEquals(savedCouponEntity.getStatus(), couponEntity.getStatus());
        assertNull(couponEntity.getUserNo());
    }

    @Test
    void user에게_할당되지_않은_쿠폰_top1_조회_실패() {
        testEntityManager.remove(savedCouponEntity);
        assertThrows(RuntimeException.class, () -> {
            CouponEntity couponEntity = couponRepository.findTop1ByStatus(Status.CREATED).orElseThrow();
        });
    }
}