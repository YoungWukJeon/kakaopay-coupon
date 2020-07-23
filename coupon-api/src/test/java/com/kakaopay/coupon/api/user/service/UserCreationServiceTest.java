package com.kakaopay.coupon.api.user.service;

import com.kakaopay.coupon.api.persistence.entity.UserEntity;
import com.kakaopay.coupon.api.persistence.repository.UserRepository;
import com.kakaopay.coupon.api.user.advice.exception.UserCreationFailureException;
import com.kakaopay.coupon.api.user.model.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.BDDMockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserCreationServiceTest {
    @InjectMocks
    private UserCreationService userCreationService;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void 존재하는_id로_회원가입_실패() {
        // given
        given(userService.checkExistsById(anyString()))
                .willReturn(true);

        // then
        assertThrows(UserCreationFailureException.class, () -> {
            // when
            userCreationService.save("testid", "testpass", true);
        });
    }

    @Test
    void 회원가입_성공() {
        // given
        UserEntity userEntity =
                UserEntity.builder()
                        .no(1L)
                        .id("test")
                        .password(passwordEncoder.encode("testpass"))
                        .build();

        given(userService.checkExistsById(anyString()))
                .willReturn(false);
        given(userRepository.save(any()))
                .willReturn(userEntity);

        // when
        UserDto userDto = userCreationService.save("test", "testpass", false);

        // then
        assertEquals(userEntity.getNo(), userDto.getNo());
        assertEquals(userEntity.getId(), userDto.getId());
        assertEquals(userEntity.getPassword(), userDto.getPassword());
    }

    @Test
    void passwordEncoder_테스트() {
        String rawPassword = "testpass";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }
}