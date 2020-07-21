package com.kakaopay.coupon.api.coupon;

import com.kakaopay.coupon.api.common.ApiService;
import com.kakaopay.coupon.api.common.model.ApiResponse;
import com.kakaopay.coupon.api.coupon.model.response.CouponCodeResponse;
import com.kakaopay.coupon.api.coupon.service.CouponCreationService;
import com.kakaopay.coupon.api.coupon.service.CouponService;
import com.kakaopay.coupon.api.coupon.service.CouponUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/coupon")
public class CouponController {
    @Autowired
    private ApiService apiService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponCreationService couponCreationService;
    @Autowired
    private CouponUpdateService couponUpdateService;

    @GetMapping
    public ApiResponse getCoupons() {
        return apiService.createSuccessResponse(
                couponService.findAll());
    }

    @GetMapping(value = "/{code}")
    public ApiResponse getCouponByCode(@PathVariable String code) {
        return apiService.createSuccessResponse(
                couponService.findByCode(code));
    }

    @GetMapping(value = "/creation")
    public ApiResponse createCoupon() {
        return apiService.createSuccessResponse(
                couponCreationService.save());
    }

    @GetMapping(value = "/creation/count/{count}")
    public ApiResponse createCoupons(@PathVariable Integer count) {
        return apiService.createSuccessResponse(
                couponCreationService.saves(count));
    }

    @PutMapping(value = "/user/{userNo}")
    public ApiResponse publishToUser(@PathVariable Long userNo) {
        return apiService.createSuccessResponse(
                CouponCodeResponse.from(
                    couponUpdateService.publishToUser(userNo)));
    }

    @GetMapping(value = "/user/{userNo}")
    public ApiResponse getCouponsByUserNo(@PathVariable Long userNo) {
        return apiService.createSuccessResponse(
                couponService.findAllByUserNo(userNo));
    }

    @PutMapping(value = "/{code}/use")
    public ApiResponse useCoupon(@PathVariable String code) {
        return apiService.createSuccessResponse(
                couponUpdateService.useCoupon(code));
    }

    @PutMapping(value = "/{code}/cancel")
    public ApiResponse cancelCoupon(@PathVariable String code) {
        return apiService.createSuccessResponse(
                couponUpdateService.cancelCoupon(code));
    }

    @GetMapping(value = "/today/expiration")
    public ApiResponse getExpiredCouponsToday() {
        return apiService.createSuccessResponse(
                couponService.findAllByExpirationDateBetweenToday(LocalDateTime.now()));
    }
}
