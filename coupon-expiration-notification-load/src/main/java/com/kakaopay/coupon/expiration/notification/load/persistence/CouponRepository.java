package com.kakaopay.coupon.expiration.notification.load.persistence;

import static com.kakaopay.coupon.expiration.notification.load.persistence.CouponEntity.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponRepository {
    List<CouponEntity> findAllByStatusAndExpirationDateBetween(Status status, LocalDateTime startDate, LocalDateTime endDate);
}
