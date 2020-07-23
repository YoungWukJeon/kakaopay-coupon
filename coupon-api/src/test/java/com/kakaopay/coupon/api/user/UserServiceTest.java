package com.kakaopay.coupon.api.user;

import com.kakaopay.coupon.api.persistence.entity.UserEntity;
import com.kakaopay.coupon.api.persistence.repository.UserRepository;
import com.kakaopay.coupon.api.user.advice.exception.UserNotFoundException;
import com.kakaopay.coupon.api.user.model.UserDto;
import com.kakaopay.coupon.api.user.service.UserService;
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
        assertEquals(userEntity.getNo(), userDto.getNo());
    }

    @Test
    void 존재하지_않는_no로_user_조회() {
        // given
        Long no = anyLong();
        given(userRepository.findById(no))
                .willReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> {
            // when
            userService.findByNo(no);
        });
    }

    @Test
    void 존재한는_id로_user_조회() {
        // given
        String id = anyString();
        UserEntity userEntity =
                UserEntity.builder()
                        .id("test")
                        .build();

        given(userRepository.findById(id))
                .willReturn(Optional.of(userEntity));

        // when
        UserDto userDto = userService.findById(id);

        // then
        assertEquals(userEntity.getId(), userDto.getId());
    }

    @Test
    void 존재하지_않는_id로_user_조회() {
        // given
        String id = anyString();
        given(userRepository.findById(id))
                .willReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> {
            // when
            userService.findById(id);
        });
    }

    @Test
    void user_no_존재_여부_체크_true() {
        // given
        Long no = anyLong();
        given(userRepository.existsByNo(no))
                .willReturn(true);

        // then
        assertDoesNotThrow(() -> {
            // when
            userService.checkExistsByNo(anyLong());
        });
    }

    @Test
    void user_no_존재_여부_체크_false() {
        // given
        Long no = anyLong();
        given(userRepository.existsByNo(no))
                .willReturn(false);

        // then
        assertThrows(UserNotFoundException.class, () -> {
            // when
            userService.checkExistsByNo(anyLong());
        });
    }

    @Test
    void user_id_존재_여부_체크_true() {
        // given
        String id = anyString();
        given(userRepository.existsById(id))
                .willReturn(true);

        // when, then
        assertTrue(userService.checkExistsById(id));
    }

    @Test
    void user_id_존재_여부_체크_false() {
        // given
        String id = anyString();
        given(userRepository.existsById(id))
                .willReturn(false);

        // when, then
        assertFalse(userService.checkExistsById(id));
    }
}