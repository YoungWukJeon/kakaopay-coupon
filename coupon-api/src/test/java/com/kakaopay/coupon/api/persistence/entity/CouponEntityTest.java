package com.kakaopay.coupon.api.persistence.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static com.kakaopay.coupon.api.persistence.entity.CouponEntity.Status;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class CouponEntityTest {
//    @PersistenceContext
//    private EntityManager testEntityManager;

    @Transactional
    @Test
    void status_enumerated_문자열_검증() {
        CouponEntity couponEntity =
                CouponEntity.builder()
                        .no(2L)
                        .code("test_code")
                        .createdDate(LocalDateTime.now())
                        .status(Status.CREATED)
                        .build();

//        testEntityManager.persist(couponEntity);
//        testEntityManager.flush();
//        testEntityManager.clear();

//        CouponEntity entity = testEntityManager.find(CouponEntity.class, 2L);
        assertEquals("CREATED", couponEntity.getStatus().toString());
    }
}