package com.kakaopay.coupon.api.auth;

import com.kakaopay.coupon.api.auth.model.request.LoginRequest;
import com.kakaopay.coupon.api.auth.model.response.LoginResponse;
import com.kakaopay.coupon.api.common.ApiService;
import com.kakaopay.coupon.api.common.jwt.JwtProvider;
import com.kakaopay.coupon.api.common.model.ApiResponse;
import com.kakaopay.coupon.api.user.model.UserDto;
import com.kakaopay.coupon.api.auth.model.request.RegistrationRequest;
import com.kakaopay.coupon.api.user.service.UserCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    private UserCreationService userCreationService;
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private ApiService apiService;

    @PostMapping(value = "/signup", consumes = "application/json")
    private ApiResponse registration(@RequestBody RegistrationRequest registrationRequest, @RequestParam(required = false, defaultValue = "false") boolean admin) {
        UserDto userDto = userCreationService.save(registrationRequest.getId(), registrationRequest.getPassword(), admin);
        return apiService.createSuccessResponse(
                LoginResponse.from(
                        jwtProvider.createToken(userDto.getNo(), userDto.getRoles())));
    }

    @PostMapping(value = "/signin", consumes = "application/json")
    public ApiResponse login(@RequestBody LoginRequest loginRequest) {
        UserDto userDto = authService.login(loginRequest.getId(), loginRequest.getPassword());
        return apiService.createSuccessResponse(
                LoginResponse.from(
                        jwtProvider.createToken(userDto.getNo(), userDto.getRoles())));
    }
}
