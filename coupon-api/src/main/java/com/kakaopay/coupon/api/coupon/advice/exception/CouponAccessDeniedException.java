package com.kakaopay.coupon.api.coupon.advice.exception;

public class CouponAccessDeniedException extends RuntimeException {
    public CouponAccessDeniedException(String msg) {
        super(msg);
    }

    public CouponAccessDeniedException() {
        this("쿠폰에 접근할 권한이 없습니다.");
    }
}
