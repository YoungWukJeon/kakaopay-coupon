package com.kakaopay.coupon.api.user.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {
    private final Long no;
    private final String id;
    private final String password;
    private final String salt;

    // TODO: 2020-07-21 이거 UserEntity 파라미터로 받는걸로 바꾸고, 테스트 코드 작성 필요
    public static UserDto from(Long no, String id, String password, String salt) {
        return new UserDto(no, id, password, salt);
    }
}
