package com.kakaopay.coupon.api.common;

import com.kakaopay.coupon.api.common.model.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public class ApiService {
    public ApiResponse createSuccessResponse(Object data) {
        return ApiResponse.from(200, "성공", data);
    }

    public ApiResponse createFailureResponse(int code, String message, Object data) {
        return ApiResponse.from(code, message, data);
    }
}
