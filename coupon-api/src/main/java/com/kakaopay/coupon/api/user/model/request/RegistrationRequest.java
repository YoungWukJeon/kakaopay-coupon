package com.kakaopay.coupon.api.user.model.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RegistrationRequest {
    private String id;
    private String password;
}
