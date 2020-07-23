package com.kakaopay.coupon.expiration.notification.load.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationDto {
    private final String code;
    private final String notificationMessage;
    private final LocalDateTime expirationDate;
    private final Long userNo;

    public static NotificationDto from(String code, String notificationMessage, LocalDateTime expirationDate, Long userNo) {
        return new NotificationDto(code, notificationMessage, expirationDate, userNo);
    }
}
