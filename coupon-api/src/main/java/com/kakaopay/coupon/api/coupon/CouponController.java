package com.kakaopay.coupon.api.coupon;

import com.kakaopay.coupon.api.common.model.ApiResponse;
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
    private CouponService couponService;
    @Autowired
    private CouponCreationService couponCreationService;
    @Autowired
    private CouponUpdateService couponUpdateService;

    @GetMapping
    public ApiResponse getAllCoupons() {
        return couponService.findAll();
    }

    @GetMapping(value = "/{code}")
    public ApiResponse getCouponByCode(@PathVariable String code) {
        return couponService.findByCode(code);
    }

    @GetMapping(value = "/creation")
    public ApiResponse createCoupon() {
        return couponCreationService.save();
    }

    @GetMapping(value = "/creation/count/{count}")
    public ApiResponse createCoupons(@PathVariable Long count) {
        return couponCreationService.saves(count);
    }

    @PutMapping(value = "/user/{userNo}")
    public ApiResponse publishToUser(@PathVariable Long userNo) {
        return couponUpdateService.publishToUser(userNo);
    }

    @GetMapping(value = "/user/{userNo}")
    public ApiResponse getCouponByUserNo(@PathVariable Long userNo) {
        return couponService.findByUserNo(userNo);
    }

    @PutMapping(value = "/{code}/use")
    public ApiResponse useCoupon(@PathVariable String code) {
        return couponUpdateService.useCoupon(code);
    }

    @PutMapping(value = "/{code}/cancel")
    public ApiResponse cancelCoupon(@PathVariable String code) {
        return couponUpdateService.cancelCoupon(code);
    }

    @GetMapping(value = "/today/expiration")
    public ApiResponse getExpiredCouponsToday() {
        return couponService.findAllByExpirationDateBetweenToday(LocalDateTime.now());
    }
}
