package com.kakaopay.coupon.api.user;

import com.kakaopay.coupon.api.persistence.entity.UserEntity;
import com.kakaopay.coupon.api.persistence.repository.UserRepository;
import com.kakaopay.coupon.api.user.model.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Test
    void 존재하는_no로_user_조회() {
        // given
        Long no = anyLong();
        UserEntity userEntity =
                UserEntity.builder()
                        .no(no)
                        .build();

        given(userRepository.findById(no))
                .willReturn(Optional.of(userEntity));

        // when
        UserDto userDto = userService.findByNo(no);

        // then
        assertEquals(userDto.getNo(), userEntity.getNo());
    }

    @Test
    void 존재하지_않는_no로_user_조회() {
        // given
        Long no = anyLong();
        given(userRepository.findById(no))
                .willReturn(Optional.empty());

        // then
        assertThrows(RuntimeException.class, () -> {
            // when
            userService.findByNo(no);
        });
    }

    @Test
    void user_no_존재_여부_체크_true() {
        // given
        Long no = anyLong();
        given(userRepository.existsById(no))
                .willReturn(true);

        // then
        assertDoesNotThrow(() -> {
            // when
            userService.checkExistsById(anyLong());
        });
    }

    @Test
    void user_no_존재_여부_체크_false() {
        // given
        Long no = anyLong();
        given(userRepository.existsById(no))
                .willReturn(false);

        // then
        assertThrows(RuntimeException.class, () -> {
            // when
            userService.checkExistsById(anyLong());
        });
    }
}