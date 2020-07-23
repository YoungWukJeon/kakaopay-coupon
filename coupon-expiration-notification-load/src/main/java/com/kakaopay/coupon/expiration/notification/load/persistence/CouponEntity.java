package com.kakaopay.coupon.expiration.notification.load.persistence;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class CouponEntity {
    private Long no;
    private String code;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
    private LocalDateTime usedDate;
    private LocalDateTime expirationDate;
    private Status status;
    private Long userNo;

    public enum Status {
        CREATED, PUBLISHED, USED, EXPIRED
    }

    @Builder
    public CouponEntity(Long no, String code,
                        LocalDateTime createdDate, LocalDateTime publishedDate,
                        LocalDateTime usedDate, LocalDateTime expirationDate,
                        Status status, Long userNo) {
        this.no = no;
        this.code = code;
        this.createdDate = createdDate;
        this.publishedDate = publishedDate;
        this.usedDate = usedDate;
        this.expirationDate = expirationDate;
        this.status = status;
        this.userNo = userNo;
    }
}
