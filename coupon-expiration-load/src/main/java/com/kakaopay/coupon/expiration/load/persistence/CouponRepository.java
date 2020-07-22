package com.kakaopay.coupon.expiration.load.persistence;

import static com.kakaopay.coupon.expiration.load.persistence.CouponEntity.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponRepository {
    List<CouponEntity> findAllByStatusAndExpirationDateBefore(Status status, LocalDateTime now);
    Integer updateAllByStatusAndExpirationDateBefore(Status afterStatus, Status beforeStatus, LocalDateTime now);
}
