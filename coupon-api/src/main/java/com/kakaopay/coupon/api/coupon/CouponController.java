package com.kakaopay.coupon.api.coupon;

import com.kakaopay.coupon.api.common.model.ApiResponse;
import com.kakaopay.coupon.api.coupon.service.CouponCreationService;
import com.kakaopay.coupon.api.coupon.service.CouponService;
import com.kakaopay.coupon.api.coupon.service.CouponUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping(value = "/user")
    public ApiResponse publishToUser(@RequestBody Long userNo) {
        return couponUpdateService.publishToUser(userNo);
    }
}
