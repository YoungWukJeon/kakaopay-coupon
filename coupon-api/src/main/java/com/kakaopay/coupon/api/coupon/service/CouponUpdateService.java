package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.coupon.model.CouponDto;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity.Status;
import com.kakaopay.coupon.api.persistence.repository.CouponRepository;
import com.kakaopay.coupon.api.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CouponUpdateService {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserService userService;

    // TODO: 2020-07-20 예외 처리
    @Transactional
    public CouponDto publishToUser(Long userNo) {
        userService.checkExistsById(userNo);
        return CouponDto.from(
                couponRepository.saveAndFlush(
                        couponRepository.findTop1ByStatus(Status.CREATED)
                                .orElseThrow()
                                .publishToUser(userNo)));
    }

    // TODO: 2020-07-20 예외 처리
    @Transactional
    public CouponDto useCoupon(String code) {
        return CouponDto.from(
                couponRepository.saveAndFlush(
                        couponRepository.findByCodeAndStatus(code, Status.PUBLISHED)
                                .orElseThrow()
                                .useCoupon()));
    }

    // TODO: 2020-07-21 예외 처리
    @Transactional
    public CouponDto cancelCoupon(String code) {
        return CouponDto.from(
                couponRepository.saveAndFlush(
                        couponRepository.findByCodeAndStatus(code, Status.USING)
                                .orElseThrow()
                                .cancelCoupon()));
    }
}
