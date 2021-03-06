package com.kakaopay.coupon.api.persistence.repository;

import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CouponRepositoryTest {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    private CouponEntity savedCouponEntity;

    @BeforeEach
    void setUp() throws Exception {
        savedCouponEntity =
                CouponEntity.builder()
                        .code("test-coupon")
                        .build();
    }

    @Test
    void 존재하는_코드로_쿠폰_조회() {
        testEntityManager.persistAndFlush(savedCouponEntity);
        CouponEntity couponEntity = couponRepository.findByCode("test-coupon").orElseThrow();
        assertEquals(savedCouponEntity.getCode(), couponEntity.getCode());
    }

    @Test
    void 존재하지_않는_코드로_쿠폰_조회() {
        assertThrows(RuntimeException.class, () -> {
            couponRepository.findByCode("test-coupon").orElseThrow();
        });
    }

    @Test
    void 생성만_된_쿠폰_조회_성공() {
        testEntityManager.persistAndFlush(savedCouponEntity);
        CouponEntity couponEntity = couponRepository.findByStatus(Status.CREATED).orElseThrow();
        assertEquals(savedCouponEntity.getStatus(), couponEntity.getStatus());
    }

    @Test
    void 생성만_된_쿠폰_조회_실패() {
        assertThrows(RuntimeException.class, () -> {
            couponRepository.findByStatus(Status.CREATED).orElseThrow();
        });
    }

    @Test
    void user에게_할당되지_않은_쿠폰_top1_조회_성공() {
        testEntityManager.persistAndFlush(savedCouponEntity);
        CouponEntity couponEntity = couponRepository.findByStatus(Status.CREATED).orElseThrow();
        assertEquals(savedCouponEntity.getCode(), couponEntity.getCode());
        assertEquals(savedCouponEntity.getStatus(), couponEntity.getStatus());
        assertNull(couponEntity.getUserNo());
    }

    @Test
    void user에게_할당되지_않은_쿠폰_top1_조회_실패() {
        assertThrows(RuntimeException.class, () -> {
            couponRepository.findTop1ByStatus(Status.CREATED).orElseThrow();
        });
    }

    @Test
    void 존재하는_user의_쿠폰_목록_조회() {
        Long userNo = 1L;
        testEntityManager.persistAndFlush(savedCouponEntity);
        savedCouponEntity.publishToUser(userNo);

        List<CouponEntity> couponEntities = couponRepository.findAllByUserNo(userNo);
        CouponEntity couponEntity = couponEntities.get(0);

        assertEquals(savedCouponEntity.getNo(), couponEntity.getNo());
        assertEquals(savedCouponEntity.getCode(), couponEntity.getCode());
        assertEquals(savedCouponEntity.getStatus(), couponEntity.getStatus());
        assertEquals(savedCouponEntity.getUserNo(), couponEntity.getUserNo());
    }

    @Test
    void 특정_status와_특정_날짜_구간의_만료일에_해당하는_쿠폰_목록_조회() {
        testEntityManager.persistAndFlush(savedCouponEntity);
        savedCouponEntity.publishToUser(1L);
        savedCouponEntity.expireCoupon();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        LocalDateTime endDate = savedCouponEntity.getExpirationDate().plusHours(1);

        List<CouponEntity> expectedCouponEntities = List.of(savedCouponEntity);

        List<CouponEntity> couponEntities = couponRepository.findAllByStatusAndExpirationDateBetween(Status.EXPIRED, startDate, endDate);
        assertIterableEquals(expectedCouponEntities, couponEntities);
    }
}