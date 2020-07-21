package com.kakaopay.coupon.api.coupon.advice;

import com.kakaopay.coupon.api.common.ApiService;
import com.kakaopay.coupon.api.common.model.ApiResponse;
import com.kakaopay.coupon.api.common.model.ExceptionDto;
import com.kakaopay.coupon.api.coupon.advice.exception.CouponCodeGenerationException;
import com.kakaopay.coupon.api.coupon.advice.exception.CouponNotFoundByStatusException;
import com.kakaopay.coupon.api.coupon.advice.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CouponExceptionAdvice {
    @Autowired
    private ApiService apiService;

    @ExceptionHandler(CouponNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponse couponNotFound(HttpServletRequest request, CouponNotFoundException exception) {
        return generateInternalServerError(request.getServletPath(), exception.getMessage());
    }

    @ExceptionHandler(CouponNotFoundByStatusException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponse couponNotFoundByStatus(HttpServletRequest request, CouponNotFoundByStatusException exception) {
        return generateInternalServerError(request.getServletPath(), exception.getMessage());
    }

    @ExceptionHandler(CouponCodeGenerationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponse couponCodeGeneration(HttpServletRequest request, CouponCodeGenerationException exception) {
        return generateInternalServerError(request.getServletPath(), exception.getMessage());
    }

    private ApiResponse generateInternalServerError(String contextPath, String message) {
        return apiService.createFailureResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ExceptionDto.from(contextPath, message));
    }
}
