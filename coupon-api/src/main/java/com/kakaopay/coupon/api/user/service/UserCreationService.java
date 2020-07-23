package com.kakaopay.coupon.api.user.service;

import com.kakaopay.coupon.api.persistence.entity.UserEntity;
import com.kakaopay.coupon.api.persistence.repository.UserRepository;
import com.kakaopay.coupon.api.user.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserCreationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // TODO: 2020-07-23 예외 처리
    public UserDto save(String id, String password) {
        if (userService.checkExistsById(id)) {
            throw new RuntimeException();
        }
        return UserDto.from(
                userRepository.save(
                        UserEntity.builder()
                                .id(id)
                                .password(passwordEncoder.encode(password))
                                .build()));
    }
}
