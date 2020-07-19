package com.kakaopay.coupon.api.coupon;

import com.kakaopay.coupon.api.common.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponCreationService couponCreationService;

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
}
