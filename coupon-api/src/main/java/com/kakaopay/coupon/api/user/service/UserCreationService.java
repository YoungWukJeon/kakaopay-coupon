package com.kakaopay.coupon.api.user.service;

import com.kakaopay.coupon.api.persistence.entity.UserEntity;
import com.kakaopay.coupon.api.persistence.repository.UserRepository;
import com.kakaopay.coupon.api.user.advice.exception.UserCreationFailureException;
import com.kakaopay.coupon.api.user.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCreationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // TODO: 테스트 코드
    public UserDto save(String id, String password, boolean admin) {
        if (userService.checkExistsById(id)) {
            throw new UserCreationFailureException();
        }
        List<String> roles = new ArrayList<> ();
        roles.add("ROLE_USER");

        if (admin) {
            roles.add("ROLE_ADMIN");
        }

        return UserDto.from(
                userRepository.save(
                        UserEntity.builder()
                                .id(id)
                                .password(passwordEncoder.encode(password))
                                .roles(roles)
                                .build()));
    }
}
