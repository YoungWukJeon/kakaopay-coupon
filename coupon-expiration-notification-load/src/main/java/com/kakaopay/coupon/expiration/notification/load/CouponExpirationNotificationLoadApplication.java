package com.kakaopay.coupon.expiration.notification.load;

import com.kakaopay.coupon.expiration.notification.load.persistence.CouponEntity.Status;
import com.kakaopay.coupon.expiration.notification.load.persistence.CouponRepository;
import com.kakaopay.coupon.expiration.notification.load.persistence.CouponRepositoryImpl;
import com.kakaopay.coupon.expiration.notification.load.persistence.DataSource;

import java.sql.SQLException;
import java.time.LocalDateTime;

// TODO: 2020-07-23 파라미터로 접속 정보를 받을 수 있게? 그리고 알림을 받을 수 있는 날짜도 조정할 수 있게
public class CouponExpirationNotificationLoadApplication {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        long afterDays = 3L;
        DataSource dataSource = new DataSource(
                "jdbc:mysql://localhost:3307/coupon_system?serverTimezone=Asia/Seoul&characterEncoding=UTF-8",
                "couponadmin",
                "couponpass");
        dataSource.connect();
        CouponRepository couponRepository = new CouponRepositoryImpl(dataSource, "coupon");
        LocalDateTime afterNDays = LocalDateTime.now().plusDays(afterDays);
        LocalDateTime startDate = LocalDateTime.of(afterNDays.getYear(), afterNDays.getMonth(), afterNDays.getDayOfMonth(), 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(afterNDays.getYear(), afterNDays.getMonth(), afterNDays.getDayOfMonth(), 23, 59, 59);

        couponRepository.findAllByStatusAndExpirationDateBetween(Status.EXPIRED, startDate, endDate)
                .forEach(System.out::println);
        dataSource.disconnect();
    }
}
