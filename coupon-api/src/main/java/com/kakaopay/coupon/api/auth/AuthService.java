package com.kakaopay.coupon.api.auth;

import com.kakaopay.coupon.api.user.advice.exception.UserNotFoundException;
import com.kakaopay.coupon.api.user.model.UserDto;
import com.kakaopay.coupon.api.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// TODO: 2020-07-24 테스트 코드
@Service
public class AuthService {
    @Autowired
    private UserService userService;

    public UserDto login(String id, String password) {
        UserDto userDto = userService.findById(id);
        if (!matchPassword(password, userDto.getPassword())) {
            throw new UserNotFoundException();
        }
        return userDto;
    }

    private boolean matchPassword(String rawPassword, String encodedPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
