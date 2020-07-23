package com.kakaopay.coupon.api.user;

import com.kakaopay.coupon.api.common.ApiService;
import com.kakaopay.coupon.api.common.model.ApiResponse;
import com.kakaopay.coupon.api.user.model.request.RegistrationRequest;
import com.kakaopay.coupon.api.user.service.UserCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserCreationService userCreationService;
    @Autowired
    private ApiService apiService;

    @PostMapping(consumes = "application/json")
    private ApiResponse registration(@RequestBody RegistrationRequest registrationRequest) {
        return apiService.createSuccessResponse(
                userCreationService.save(registrationRequest.getId(), registrationRequest.getPassword()));
    }
}
