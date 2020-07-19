package com.kakaopay.coupon.api.common.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse {
    private final int status;
    private final String message;
    private final Object data;

    public static ApiResponse from(int status, String message, Object data) {
        return new ApiResponse(status, message, data);
    }
}
