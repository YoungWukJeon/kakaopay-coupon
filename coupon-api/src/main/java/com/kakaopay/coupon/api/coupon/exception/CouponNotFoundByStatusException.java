package com.kakaopay.coupon.api.coupon.exception;

import com.kakaopay.coupon.api.persistence.entity.CouponEntity.Status;

public class CouponNotFoundByStatusException extends RuntimeException {
    public CouponNotFoundByStatusException(String msg) {
        super(msg);
    }

    public CouponNotFoundByStatusException(Status status) {
        this("'" + status + "' 상태의 쿠폰이 없습니다.");
    }
}
