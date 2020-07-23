package com.kakaopay.coupon.api.user.model;

import com.kakaopay.coupon.api.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {
    @Test
    void entity를_dto로_변환() {
        UserEntity userEntity =
                UserEntity.builder()
                        .no(1L)
                        .id("testid")
                        .password("testpass")
                        .build();
        UserDto userDto = UserDto.from(userEntity);

        assertEquals(userEntity.getNo(), userDto.getNo());
        assertEquals(userEntity.getId(), userDto.getId());
        assertEquals(userEntity.getPassword(), userDto.getPassword());
    }
}