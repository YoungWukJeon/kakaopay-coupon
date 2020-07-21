package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.coupon.model.CouponDto;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity.Status;
import com.kakaopay.coupon.api.persistence.repository.CouponRepository;
import com.kakaopay.coupon.api.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CouponUpdateServiceTest {
    @InjectMocks
    private CouponUpdateService couponUpdateService;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private UserService userService;

    @Test
    void 시간_차이_구하기() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after = now.plusHours(10);
        assertEquals(10, ChronoUnit.HOURS.between(now, after));
    }

    @Test
    void 쿠폰_유저_발행_성공() {
        // given
        Long userNo = 0L;
        Status status = Status.CREATED;
        CouponEntity couponEntity =
                CouponEntity.builder()
                        .code("test-code")
                        .status(status)
                        .build();

        willDoNothing()
                .given(userService)
                .checkExistsById(userNo);
        given(couponRepository.findTop1ByStatus(status))
                .willReturn(Optional.of(couponEntity));
        given(couponRepository.saveAndFlush(any()))
                .willReturn(couponEntity);

        // when
        CouponDto couponDto = couponUpdateService.publishToUser(userNo);
        System.out.println(couponDto);

        // then
        assertNotNull(couponDto.getPublishedDate());
        assertNotNull(couponDto.getExpirationDate());
        assertEquals(1,
                ChronoUnit.HOURS.between(
                        couponDto.getPublishedDate(), couponDto.getExpirationDate()));
        assertEquals(Status.PUBLISHED, couponDto.getStatus());
        assertEquals(userNo, couponDto.getUserNo());
    }

    @Test
    void 발행_가능한_쿠폰이_없어서_발행_실패() {
        // given
        Long userNo = 0L;
        Status status = Status.CREATED;
        willDoNothing()
                .given(userService)
                .checkExistsById(userNo);
        given(couponRepository.findTop1ByStatus(status))
                .willReturn(Optional.empty());

        // then
        assertThrows(RuntimeException.class, () -> {
            // when
            couponUpdateService.publishToUser(userNo);
        });
    }

    @Test
    void 쿠폰_사용_성공() {
        // given
        CouponEntity couponEntity =
                CouponEntity.builder()
                        .code("test-code")
                        .status(Status.PUBLISHED)
                        .build();
        CouponEntity usingCouponEntity =
                CouponEntity.builder()
                        .code("test-code")
                        .status(Status.USING)
                        .build();

        given(couponRepository.findByCodeAndStatus(anyString(), any()))
                .willReturn(Optional.of(couponEntity));
        given(couponRepository.saveAndFlush(any()))
                .willReturn(usingCouponEntity);

        // when
        CouponDto couponDto = couponUpdateService.useCoupon("test-code");

        // then
        assertEquals("test-code", couponDto.getCode());
        assertEquals(Status.USING, couponDto.getStatus());
    }

    @Test
    void 존재하지_않는_쿠폰으로_사용_실패() {
        // given
        given(couponRepository.findByCodeAndStatus(anyString(), any()))
                .willReturn(Optional.empty());

        // then
        assertThrows(RuntimeException.class, () -> {
            // when
            couponUpdateService.useCoupon("test-code");
        });
    }

    @Test
    void 쿠폰_취소_성공() {
        // given
        CouponEntity couponEntity =
                CouponEntity.builder()
                        .code("test-code")
                        .status(Status.USING)
                        .build();
        CouponEntity usingCouponEntity =
                CouponEntity.builder()
                        .code("test-code")
                        .status(Status.PUBLISHED)
                        .build();

        given(couponRepository.findByCodeAndStatus(anyString(), any()))
                .willReturn(Optional.of(couponEntity));
        given(couponRepository.saveAndFlush(any()))
                .willReturn(usingCouponEntity);

        // when
        CouponDto couponDto = couponUpdateService.cancelCoupon("test-code");

        // then
        assertEquals("test-code", couponDto.getCode());
        assertEquals(Status.PUBLISHED, couponDto.getStatus());
    }

    @Test
    void 존재하지_않는_쿠폰이거나_status가_USING이_아니어서_취소_실패() {
        // given
        given(couponRepository.findByCodeAndStatus(anyString(), any()))
                .willReturn(Optional.empty());

        // then
        assertThrows(RuntimeException.class, () -> {
            // when
            couponUpdateService.cancelCoupon("test-code");
        });
    }
}