package com.kakaopay.coupon.expiration.notification.load;

import com.kakaopay.coupon.expiration.notification.load.kafka.Producer;
import com.kakaopay.coupon.expiration.notification.load.model.NotificationDto;
import com.kakaopay.coupon.expiration.notification.load.persistence.CouponEntity;
import com.kakaopay.coupon.expiration.notification.load.persistence.CouponEntity.Status;
import com.kakaopay.coupon.expiration.notification.load.persistence.CouponRepository;
import com.kakaopay.coupon.expiration.notification.load.persistence.CouponRepositoryImpl;
import com.kakaopay.coupon.expiration.notification.load.persistence.DataSource;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

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

        List<CouponEntity> couponEntities = couponRepository.findAllByStatusAndExpirationDateBetween(Status.PUBLISHED, startDate, endDate);
        dataSource.disconnect();
        couponEntities.forEach(System.out::println);

        String topicName = "notification";
        Producer producer = new Producer();
        couponEntities.parallelStream()
                .map(e ->
                        NotificationDto.from(
                                e.getCode(),
                                "쿠폰이 3일 후 만료됩니다",
                                e.getExpirationDate(),
                                e.getUserNo()))
                .forEach(n -> producer.publish(topicName, n.getExpirationDate().getDayOfWeek().getValue(), n));
        producer.flush();
    }
}
