package com.kakaopay.coupon.api.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String msg) {
        super(msg);
    }

    public UserNotFoundException() {
        this("해당 유저를 찾을 수 없습니다.");
    }
}
