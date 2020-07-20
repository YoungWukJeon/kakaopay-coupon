package com.kakaopay.coupon.api.user;

import com.kakaopay.coupon.api.persistence.entity.UserEntity;
import com.kakaopay.coupon.api.persistence.repository.UserRepository;
import com.kakaopay.coupon.api.user.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // TODO: 2020-07-20 예외 처리
    public UserDto findByNo(Long no) {
        return convertUserEntityToUserResponse(
                userRepository.findById(no)
                        .orElseThrow());
    }

    // TODO: 2020-07-20 예외 처리, 여기 로직 변경하는게 나을듯
    public void checkExistsById(Long no) {
        if (userRepository.existsById(no)) {
            return;
        }
        throw new RuntimeException();
    }

    private UserDto convertUserEntityToUserResponse(UserEntity userEntity) {
        return UserDto.from(userEntity.getNo(), userEntity.getId(), userEntity.getPassword(), userEntity.getSalt());
    }
}
