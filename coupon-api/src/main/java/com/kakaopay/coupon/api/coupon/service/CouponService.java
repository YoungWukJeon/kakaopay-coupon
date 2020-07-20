package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.common.ApiService;
import com.kakaopay.coupon.api.common.model.ApiResponse;
import com.kakaopay.coupon.api.coupon.model.response.CouponResponse;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import com.kakaopay.coupon.api.persistence.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
                        .map(this::convertCouponEntityToCouponResponse)
                        .collect(Collectors.toList())
        );
    }

    // TODO: 2020-07-18 예외 처리
    public ApiResponse findByCode(String code) {
        CouponEntity couponEntity = couponRepository.findByCode(code).orElseThrow();

        return apiService.createSuccessResponse(
                convertCouponEntityToCouponResponse(couponEntity));
    }

    // TODO: 2020-07-20 예외 처리
    public ApiResponse findByUserNo(Long userNo) {
        CouponEntity couponEntity = couponRepository.findByUserNo(userNo).orElseThrow();

        return apiService.createSuccessResponse(
                convertCouponEntityToCouponResponse(couponEntity));
    }

    public ApiResponse findAllByExpirationDateBetweenToday(LocalDateTime dateTime) {
        LocalDateTime date = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), 0, 0, 0);
        List<CouponEntity> couponEntities = couponRepository.findAllByStatusAndExpirationDateBetween(CouponEntity.Status.EXPIRED, date, dateTime);
        return apiService.createSuccessResponse(
                couponEntities.stream()
                        .map(this::convertCouponEntityToCouponResponse)
                        .collect(Collectors.toList()));
    }

    // TODO: 2020-07-19 테스트 코드 필요
    private CouponResponse convertCouponEntityToCouponResponse(CouponEntity couponEntity) {
        return CouponResponse.from(
                couponEntity.getNo(), couponEntity.getCode(),
                couponEntity.getCreatedDate(), couponEntity.getPublishedDate(), couponEntity.getExpirationDate(),
                couponEntity.getStatus(), couponEntity.getUserNo());
    }
}
