package com.kakaopay.coupon.api.coupon.advice.exception;

public class CouponCodeGenerationException extends RuntimeException {
    public CouponCodeGenerationException(String msg) {
        super(msg);
    }

    public CouponCodeGenerationException() {
        this("쿠폰 코드 생성에 실패했습니다.");
    }
}
