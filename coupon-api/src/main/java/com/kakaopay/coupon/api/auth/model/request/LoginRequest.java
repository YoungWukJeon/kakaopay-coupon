package com.kakaopay.coupon.api.auth.model.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginRequest {
    private String id;
    private String password;
}
