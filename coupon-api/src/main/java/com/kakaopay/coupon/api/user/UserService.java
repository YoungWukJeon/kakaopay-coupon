package com.kakaopay.coupon.api.user;

import com.kakaopay.coupon.api.persistence.entity.UserEntity;
import com.kakaopay.coupon.api.persistence.repository.UserRepository;
import com.kakaopay.coupon.api.user.model.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // TODO: 2020-07-20 예외 처리
    public UserResponse findByNo(Long no) {
        return convertUserEntityToUserResponse(
                userRepository.findById(no)
                        .orElseThrow());
    }

    // TODO: 2020-07-20 예외 처리
    public void checkExistsById(Long no) {
        if (userRepository.existsById(no)) {
            return;
        }
        throw new RuntimeException();
    }

    private UserResponse convertUserEntityToUserResponse(UserEntity userEntity) {
        return UserResponse.from(userEntity.getNo(), userEntity.getId(), userEntity.getPassword(), userEntity.getSalt());
    }
}
