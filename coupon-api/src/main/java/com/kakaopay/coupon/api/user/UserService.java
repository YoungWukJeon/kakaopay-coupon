package com.kakaopay.coupon.api.user;

import com.kakaopay.coupon.api.persistence.repository.UserRepository;
import com.kakaopay.coupon.api.user.exception.UserNotFoundException;
import com.kakaopay.coupon.api.user.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDto findByNo(Long no) {
        return UserDto.from(
                userRepository.findById(no)
                        .orElseThrow(UserNotFoundException::new));
    }

    // TODO: 2020-07-20 여기 로직 변경하는게 나을듯
    public void checkExistsById(Long no) {
        if (userRepository.existsById(no)) {
            return;
        }
        throw new UserNotFoundException();
    }
}
