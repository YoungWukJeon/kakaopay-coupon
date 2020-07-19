package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.common.ApiService;
import com.kakaopay.coupon.api.common.model.ApiResponse;
import com.kakaopay.coupon.api.coupon.model.response.CouponResponse;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
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
    @Autowired
    private ApiService apiService;

    // TODO: 2020-07-20 예외 처리
    @Transactional
    public ApiResponse publishToUser(Long userNo) {
        userService.checkExistsById(userNo);
        CouponEntity couponEntity =
                couponRepository.saveAndFlush(
                        couponRepository.findTop1ByStatus(Status.CREATED)
                                .orElseThrow()
                                .publishToUser(userNo));

        return apiService.createSuccessResponse(
                convertCouponEntityToCouponResponse(couponEntity));
    }

    private CouponResponse convertCouponEntityToCouponResponse(CouponEntity couponEntity) {
        return CouponResponse.from(
                couponEntity.getNo(), couponEntity.getCode(),
                couponEntity.getCreatedDate(), couponEntity.getPublishedDate(), couponEntity.getExpirationDate(),
                couponEntity.getStatus(), couponEntity.getUserNo());
    }
}
