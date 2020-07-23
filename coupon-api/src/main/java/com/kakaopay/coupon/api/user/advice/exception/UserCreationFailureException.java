package com.kakaopay.coupon.api.user.advice.exception;

public class UserCreationFailureException extends RuntimeException {
    public UserCreationFailureException(String msg) {
        super(msg);
    }

    public UserCreationFailureException() {
        this("중복된 id로 회원 가입에 실패했습니다.");
    }
}
