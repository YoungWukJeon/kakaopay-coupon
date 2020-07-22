package com.kakaopay.coupon.expiration.load;

import com.kakaopay.coupon.expiration.load.persistence.CouponEntity.Status;
import com.kakaopay.coupon.expiration.load.persistence.CouponRepository;
import com.kakaopay.coupon.expiration.load.persistence.CouponRepositoryImpl;
import com.kakaopay.coupon.expiration.load.persistence.DataSource;

import java.time.LocalDateTime;

// TODO: 2020-07-23 파라미터로 접속 정보를 받을 수 있게? 
public class CouponExpirationLoadApplication {
    public static void main(String[] args) {
        DataSource dataSource = new DataSource(
                "jdbc:mysql://localhost:3307/coupon_system?serverTimezone=UTC&characterEncoding=UTF-8",
                "couponadmin",
                "couponpass");
        dataSource.connect();
        CouponRepository couponRepository = new CouponRepositoryImpl(dataSource, "coupon");
        int updateCount = couponRepository.updateAllByStatusAndExpirationDateBefore(
                Status.EXPIRED, Status.PUBLISHED, LocalDateTime.now());
        System.out.println(updateCount + " rows changed.");
        dataSource.disconnect();
    }
}
