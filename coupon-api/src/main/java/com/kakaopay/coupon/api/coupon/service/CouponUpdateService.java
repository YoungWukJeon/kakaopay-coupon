package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.coupon.advice.exception.CouponNotAvailableException;
import com.kakaopay.coupon.api.coupon.advice.exception.CouponNotFoundByStatusException;
import com.kakaopay.coupon.api.coupon.model.CouponDto;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity.Status;
import com.kakaopay.coupon.api.persistence.repository.CouponRepository;
import com.kakaopay.coupon.api.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class CouponUpdateService {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public CouponDto publishToUser(Long userNo) {
        userService.checkExistsByNo(userNo);
        return CouponDto.from(
                couponRepository.saveAndFlush(
                        couponRepository.findTop1ByStatus(Status.CREATED)
                                .orElseThrow(() -> new CouponNotFoundByStatusException(Status.CREATED))
                                .publishToUser(userNo)));
    }

    @Transactional
    public CouponDto useCoupon(String code, Long userNo) {
        return CouponDto.from(
                couponRepository.saveAndFlush(
                    couponRepository.findByCode(code)
                            .filter(e -> e.getStatus() == Status.PUBLISHED)
                            .filter(e -> e.getExpirationDate().compareTo(LocalDateTime.now()) > 0)
                            .filter(e -> e.getUserNo().equals(userNo))
                            .orElseThrow(CouponNotAvailableException::new)
                            .useCoupon()));
    }

    @Transactional
    public CouponDto cancelCoupon(String code, Long userNo) {
        return CouponDto.from(
                couponRepository.saveAndFlush(
                        couponRepository.findByCode(code)
                                .filter(e -> e.getStatus() == Status.USED)
                                .filter(e -> e.getUserNo().equals(userNo))
                                .orElseThrow(() -> new CouponNotFoundByStatusException(Status.USED))
                                .cancelCoupon()));
    }
}
