package com.kakaopay.coupon.api.user.advice;

import com.kakaopay.coupon.api.common.ApiService;
import com.kakaopay.coupon.api.common.model.ApiResponse;
import com.kakaopay.coupon.api.common.model.ExceptionDto;
import com.kakaopay.coupon.api.user.advice.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class UserExceptionAdvice {
    @Autowired
    private ApiService apiService;

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponse userNotFound(HttpServletRequest request, UserNotFoundException exception) {
        return generateInternalServerError(request.getContextPath(), exception.getMessage());
    }

    private ApiResponse generateInternalServerError(String contextPath, String message) {
        return apiService.createFailureResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ExceptionDto.from(contextPath, message));
    }
}
