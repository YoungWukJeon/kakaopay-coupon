package com.kakaopay.coupon.api.coupon.advice.exception;

public class CouponStatusNotFoundException extends RuntimeException {
    public CouponStatusNotFoundException(String msg) {
        super(msg);
    }

    public CouponStatusNotFoundException() {
        this("알 수 없는 쿠폰 상태 변경 명령입니다.");
    }
}
