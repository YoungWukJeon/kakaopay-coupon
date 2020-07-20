package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.coupon.model.CouponDto;
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

    public List<CouponDto> findAll() {
        return couponRepository.findAll()
                .stream()
                .map(CouponDto::from)
                .collect(Collectors.toList());
    }

    // TODO: 2020-07-18 예외 처리
    public CouponDto findByCode(String code) {
        return CouponDto.from(
                couponRepository.findByCode(code)
                        .orElseThrow());
    }

    public List<CouponDto> findAllByUserNo(Long userNo) {
        return couponRepository.findAllByUserNo(userNo)
                .stream()
                .map(CouponDto::from)
                .collect(Collectors.toList());
    }

    public List<CouponDto> findAllByExpirationDateBetweenToday(LocalDateTime dateTime) {
        LocalDateTime date = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), 0, 0, 0);
        List<CouponEntity> couponEntities = couponRepository.findAllByStatusAndExpirationDateBetween(CouponEntity.Status.EXPIRED, date, dateTime);
        return couponEntities.stream()
                .map(CouponDto::from)
                .collect(Collectors.toList());
    }
}
