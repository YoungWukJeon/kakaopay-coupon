package com.kakaopay.coupon.api.coupon.advice.exception;

public class CouponNotAvailableException extends RuntimeException {
    public CouponNotAvailableException(String msg) {
        super(msg);
    }

    public CouponNotAvailableException() {
        this("해당 쿠폰을 사용할 수 없습니다.");
    }
}
