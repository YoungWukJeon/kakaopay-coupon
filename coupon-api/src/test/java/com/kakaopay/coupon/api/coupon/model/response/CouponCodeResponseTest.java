package com.kakaopay.coupon.api.coupon.model.response;

import com.kakaopay.coupon.api.coupon.model.CouponDto;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CouponCodeResponseTest {
    @Test
    void dto를_response로_변환() {
        CouponEntity couponEntity =
                CouponEntity.builder()
                        .code("test-code")
                        .build();
        couponEntity.publishToUser(1L);
        CouponDto couponDto = CouponDto.from(couponEntity);
        CouponCodeResponse couponCodeResponse = CouponCodeResponse.from(couponDto);

        assertEquals(couponDto.getCode(), couponCodeResponse.getCode());
        assertEquals(couponDto.getExpirationDate(), couponCodeResponse.getExpirationDate());
    }
}