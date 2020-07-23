package com.kakaopay.coupon.api.common;

import com.kakaopay.coupon.api.persistence.repository.UserRepository;
import com.kakaopay.coupon.api.user.advice.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

// TODO: 2020-07-24 테스트 코드
@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findById(Long.parseLong(username))
                .orElseThrow(UserNotFoundException::new);
    }
}
