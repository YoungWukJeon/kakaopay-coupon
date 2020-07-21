package com.kakaopay.coupon.api.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "coupon", uniqueConstraints = {
        @UniqueConstraint(name = "unique_code", columnNames = "code"),
}, indexes = {
        @Index(name = "index_expiration_date", columnList = "expiration_date"),
        @Index(name = "index_status", columnList = "status"),
        @Index(name = "index_user_no", columnList = "user_no")
})
public class CouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @Column(name = "code", length = 30, nullable = false, unique = true)
    private String code;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Column(name = "used_date")
    private LocalDateTime usedDate;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "user_no")
    private Long userNo;

    public enum Status {
        CREATED, PUBLISHED, USED, EXPIRED
    }

    @Builder
    public CouponEntity(Long no, String code, LocalDateTime createdDate, Status status) {
        this.no = no;
        this.code = code;
        this.createdDate = createdDate;
        this.status = status;
    }

    @PrePersist
    private void onCreation() {
        this.createdDate = LocalDateTime.now();
        this.status = Status.CREATED;
    }

    public CouponEntity publishToUser(Long userNo) {
        LocalDateTime now = LocalDateTime.now();
        this.publishedDate = now;
        this.expirationDate = now.plusHours(1L);
        this.status = Status.PUBLISHED;
        this.userNo = userNo;
        return this;
    }

    public CouponEntity useCoupon() {
        this.status = Status.USED;
        this.usedDate = LocalDateTime.now();
        return this;
    }

    public CouponEntity cancelCoupon() {
        this.status = Status.PUBLISHED;
        this.usedDate = null;
        return this;
    }

    public CouponEntity expireCoupon() {
        this.status = Status.EXPIRED;
        return this;
    }
}
