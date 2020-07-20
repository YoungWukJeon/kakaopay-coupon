package com.kakaopay.coupon.api.coupon.service;

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

    public List<CouponResponse> findAll() {
        return couponRepository.findAll()
                .stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }

    // TODO: 2020-07-18 예외 처리
    public CouponResponse findByCode(String code) {
        return CouponResponse.from(
                couponRepository.findByCode(code)
                        .orElseThrow());
    }

    public List<CouponResponse> findAllByUserNo(Long userNo) {
        return couponRepository.findAllByUserNo(userNo)
                .stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }

    public List<CouponResponse> findAllByExpirationDateBetweenToday(LocalDateTime dateTime) {
        LocalDateTime date = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), 0, 0, 0);
        List<CouponEntity> couponEntities = couponRepository.findAllByStatusAndExpirationDateBetween(CouponEntity.Status.EXPIRED, date, dateTime);
        return couponEntities.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }
}
