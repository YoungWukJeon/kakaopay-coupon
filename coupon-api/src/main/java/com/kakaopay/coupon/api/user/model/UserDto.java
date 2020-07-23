package com.kakaopay.coupon.api.user.model;

import com.kakaopay.coupon.api.persistence.entity.UserEntity;
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

    public static UserDto from(UserEntity userEntity) {
        return new UserDto(userEntity.getNo(), userEntity.getId(), userEntity.getPassword());
    }
}
