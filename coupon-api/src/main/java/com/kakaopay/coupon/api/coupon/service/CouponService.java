package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.common.ApiService;
import com.kakaopay.coupon.api.common.model.ApiResponse;
import com.kakaopay.coupon.api.coupon.model.response.CouponResponse;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import com.kakaopay.coupon.api.persistence.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private ApiService apiService;

    public ApiResponse findAll() {
        List<CouponEntity> couponEntities = couponRepository.findAll();

        return apiService.createSuccessResponse(
                couponEntities.stream()
                        .peek(System.out::println)
                        .map(this::convertCouponEntityToCouponResponse)
                        .peek(System.out::println)
                        .collect(Collectors.toList())
        );
    }

    // TODO: 2020-07-18 예외 처리
    public ApiResponse findByCode(String code) {
        CouponEntity couponEntity = couponRepository.findByCode(code).orElseThrow();

        return apiService.createSuccessResponse(
                convertCouponEntityToCouponResponse(couponEntity));
    }

    // TODO: 2020-07-19 테스트 코드 필요
    private CouponResponse convertCouponEntityToCouponResponse(CouponEntity couponEntity) {
        return CouponResponse.from(
                couponEntity.getNo(), couponEntity.getCode(),
                couponEntity.getCreatedDate(), couponEntity.getPublishedDate(), couponEntity.getExpirationDate(),
                couponEntity.getStatus(), couponEntity.getUserNo());
    }
}
