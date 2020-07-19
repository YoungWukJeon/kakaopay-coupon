package com.kakaopay.coupon.api.coupon.model.response;

import com.kakaopay.coupon.api.persistence.entity.CouponEntity.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponResponse {
    private final Long no;
    private final String code;
    private final LocalDateTime createdDate;
    private final LocalDateTime publishedDate;
    private final LocalDateTime expirationDate;
    private final Status status;
    private final Long userNo;

    public static CouponResponse from(Long no, String code,
                                      LocalDateTime createdDate, LocalDateTime publishedDate, LocalDateTime expirationDate,
                                      Status status, Long userNo) {
        return new CouponResponse(no, code, createdDate, publishedDate, expirationDate, status, userNo);
    }
}
