package com.kakaopay.coupon.api.auth.model.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {
    private final String token;

    public static LoginResponse from(String token) {
        return new LoginResponse(token);
    }
}
