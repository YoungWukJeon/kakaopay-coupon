package com.kakaopay.coupon.api.coupon.model;

import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponDtoTest {
    @Test
    void entity를_dto로_변환() {
        CouponEntity couponEntity =
                CouponEntity.builder()
                        .no(1L)
                        .code("test-code")
                        .createdDate(LocalDateTime.now())
                        .build();
        couponEntity.publishToUser(1L);
        couponEntity.useCoupon();
        CouponDto couponDto = CouponDto.from(couponEntity);

        assertEquals(couponEntity.getNo(), couponDto.getNo());
        assertEquals(couponEntity.getCode(), couponDto.getCode());
        assertEquals(couponEntity.getCreatedDate(), couponDto.getCreatedDate());
        assertEquals(couponEntity.getPublishedDate(), couponDto.getPublishedDate());
        assertEquals(couponEntity.getUsingDate(), couponDto.getUsingDate());
        assertEquals(couponEntity.getExpirationDate(), couponDto.getExpirationDate());
        assertEquals(couponEntity.getStatus(), couponDto.getStatus());
        assertEquals(couponEntity.getUserNo(), couponDto.getUserNo());
    }
}