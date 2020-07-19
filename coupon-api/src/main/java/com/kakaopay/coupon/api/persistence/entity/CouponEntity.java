package com.kakaopay.coupon.api.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "coupon", uniqueConstraints = {
        @UniqueConstraint(name = "unique_code", columnNames = "code")
})
public class CouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @Column(name = "code", length = 30, nullable = false)
    private String code;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

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
}
