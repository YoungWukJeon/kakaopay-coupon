package com.kakaopay.coupon.api.common.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionDto {
    private final String path;
    private final String message;

    public static ExceptionDto from(String path, String message) {
        return new ExceptionDto(path, message);
    }
}
