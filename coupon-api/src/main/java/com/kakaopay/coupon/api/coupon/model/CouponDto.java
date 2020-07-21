package com.kakaopay.coupon.api.coupon.model;

import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponDto {
    private final Long no;
    private final String code;
    private final LocalDateTime createdDate;
    private final LocalDateTime publishedDate;
    private final LocalDateTime usingDate;
    private final LocalDateTime expirationDate;
    private final Status status;
    private final Long userNo;

    // TODO: 2020-07-21 테스트 코드
    public static CouponDto from(CouponEntity couponEntity) {
        return new CouponDto(
                couponEntity.getNo(), couponEntity.getCode(),
                couponEntity.getCreatedDate(), couponEntity.getPublishedDate(),
                couponEntity.getUsingDate(), couponEntity.getExpirationDate(),
                couponEntity.getStatus(), couponEntity.getUserNo());
    }
}
