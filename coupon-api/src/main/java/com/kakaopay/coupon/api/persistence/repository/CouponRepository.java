package com.kakaopay.coupon.api.persistence.repository;

import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
    Optional<CouponEntity> findByCode(String code);
    Optional<CouponEntity> findByStatus(Status status);
    Optional<CouponEntity> findTop1ByStatus(Status status);
}
