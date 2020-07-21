package com.kakaopay.coupon.api.coupon.exception;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(String msg) {
        super(msg);
    }

    public CouponNotFoundException() {
        this("해당 코드의 쿠폰을 찾을 수 없습니다.");
    }
}
