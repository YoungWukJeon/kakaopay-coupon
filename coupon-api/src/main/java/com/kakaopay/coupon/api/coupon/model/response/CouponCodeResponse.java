package com.kakaopay.coupon.api.coupon.model.response;

import com.kakaopay.coupon.api.coupon.model.CouponDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponCodeResponse {
    private final String code;
    private final LocalDateTime expirationDate;

    public static CouponCodeResponse from(CouponDto couponDto) {
        return new CouponCodeResponse(couponDto.getCode(), couponDto.getExpirationDate());
    }
}
