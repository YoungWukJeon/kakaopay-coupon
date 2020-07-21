package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.coupon.advice.exception.CouponNotAvailableException;
import com.kakaopay.coupon.api.coupon.advice.exception.CouponNotFoundByStatusException;
import com.kakaopay.coupon.api.coupon.model.CouponDto;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity.Status;
import com.kakaopay.coupon.api.persistence.repository.CouponRepository;
import com.kakaopay.coupon.api.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
        assertThrows(CouponNotFoundByStatusException.class, () -> {
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
                        .build();
        couponEntity.publishToUser(1L);
        CouponEntity usedCouponEntity =
                CouponEntity.builder()
                        .code("test-code")
                        .build();
        usedCouponEntity.publishToUser(1L);
        usedCouponEntity.useCoupon();

        given(couponRepository.findByCode(anyString()))
                .willReturn(Optional.of(couponEntity));
        given(couponRepository.saveAndFlush(any()))
                .willReturn(usedCouponEntity);

        // when
        CouponDto couponDto = couponUpdateService.useCoupon("test-code");

        // then
        assertEquals("test-code", couponDto.getCode());
        assertEquals(Status.USED, couponDto.getStatus());
        assertEquals(1, couponDto.getExpirationDate().compareTo(LocalDateTime.now()));
    }

    @Test
    void 존재하지_않는_쿠폰으로_사용_실패() {
        // given
        given(couponRepository.findByCode(anyString()))
                .willReturn(Optional.empty());

        // then
        assertThrows(CouponNotAvailableException.class, () -> {
            // when
            couponUpdateService.useCoupon("test-code");
        });
    }

    @Test
    void status가_PUBLISHED가_아닌_쿠폰으로_사용_실패(){
        // given
        CouponEntity couponEntity =
                CouponEntity.builder()
                        .code("test-code")
                        .status(Status.USED)
                        .build();
        given(couponRepository.findByCode(anyString()))
                .willReturn(Optional.of(couponEntity));

        // then
        assertThrows(CouponNotAvailableException.class, () -> {
            // when
            couponUpdateService.useCoupon("test-code");
        });
    }

    @Test
    void 만료_기간이_지난_쿠폰으로_사용_실패() {
        // given
        CouponEntity couponEntity =
                CouponEntity.builder()
                        .code("test-code")
                        .build();
        couponEntity.publishToUser(1L);
        // 만료 기간을 테스트하기 위해 임의로 만료일을 1일 전으로 수정
        ReflectionTestUtils.setField(couponEntity, "expirationDate", LocalDateTime.now().minusYears(1L));

        given(couponRepository.findByCode(anyString()))
                .willReturn(Optional.of(couponEntity));

        // then
        assertThrows(CouponNotAvailableException.class, () -> {
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
                        .status(Status.USED)
                        .build();
        CouponEntity canceledCouponEntity =
                CouponEntity.builder()
                        .code("test-code")
                        .status(Status.PUBLISHED)
                        .build();

        given(couponRepository.findByCode(anyString()))
                .willReturn(Optional.of(couponEntity));
        given(couponRepository.saveAndFlush(any()))
                .willReturn(canceledCouponEntity);

        // when
        CouponDto couponDto = couponUpdateService.cancelCoupon("test-code");

        // then
        assertEquals("test-code", couponDto.getCode());
        assertEquals(Status.PUBLISHED, couponDto.getStatus());
    }

    @Test
    void 존재하지_않는_쿠폰으로_취소_실패() {
        // given
        given(couponRepository.findByCode(anyString()))
                .willReturn(Optional.empty());

        // then
        assertThrows(CouponNotFoundByStatusException.class, () -> {
            // when
            couponUpdateService.cancelCoupon("test-code");
        });
    }

    @Test
    void status가_USED가_아닌_쿠폰으로_취소_실패() {
        // given
        CouponEntity couponEntity =
                CouponEntity.builder()
                        .code("test-code")
                        .status(Status.PUBLISHED)
                        .build();
        given(couponRepository.findByCode(anyString()))
                .willReturn(Optional.of(couponEntity));

        // then
        assertThrows(CouponNotFoundByStatusException.class, () -> {
            // when
            couponUpdateService.cancelCoupon("test-code");
        });
    }
}