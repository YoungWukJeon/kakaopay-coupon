package com.kakaopay.coupon.api.user.model.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {
    private final Long no;
    private final String id;
    private final String password;
    private final String salt;

    public static UserResponse from(Long no, String id, String password, String salt) {
        return new UserResponse(no, id, password, salt);
    }
}
