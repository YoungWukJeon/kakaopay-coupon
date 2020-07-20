package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.common.ApiService;
import com.kakaopay.coupon.api.common.model.ApiResponse;
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
    @Mock
    private ApiService apiService;

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
        given(apiService.createSuccessResponse(any()))
                .willReturn(ApiResponse.from(200, "성공", couponEntity));

        // when
        ApiResponse apiResponse = couponUpdateService.publishToUser(userNo);
        CouponEntity responseCouponEntity = (CouponEntity) apiResponse.getData();

        // then
        assertEquals(Status.PUBLISHED, responseCouponEntity.getStatus());
        assertNotNull(responseCouponEntity.getPublishedDate());
        assertNotNull(responseCouponEntity.getExpirationDate());
        assertEquals(1,
                ChronoUnit.HOURS.between(
                        responseCouponEntity.getPublishedDate(), responseCouponEntity.getExpirationDate()));
        assertEquals(userNo, ((CouponEntity) apiResponse.getData()).getUserNo());
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
            ApiResponse apiResponse = couponUpdateService.publishToUser(userNo);
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
        given(apiService.createSuccessResponse(any()))
                .willReturn(ApiResponse.from(200, "성공", usingCouponEntity));

        // when
        ApiResponse apiResponse = couponUpdateService.useCoupon("test-code");
        CouponEntity responseCouponEntity = (CouponEntity) apiResponse.getData();

        // then
        assertEquals(Status.USING, responseCouponEntity.getStatus());
        assertEquals("test-code", ((CouponEntity) apiResponse.getData()).getCode());
    }

    @Test
    void 존재하지_않는_쿠폰으로_사용_실패() {
        // given
        given(couponRepository.findByCodeAndStatus(anyString(), any()))
                .willReturn(Optional.empty());

        // then
        assertThrows(RuntimeException.class, () -> {
            // when
            ApiResponse apiResponse = couponUpdateService.useCoupon("test-code");
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
        given(apiService.createSuccessResponse(any()))
                .willReturn(ApiResponse.from(200, "성공", usingCouponEntity));

        // when
        ApiResponse apiResponse = couponUpdateService.cancelCoupon("test-code");
        CouponEntity responseCouponEntity = (CouponEntity) apiResponse.getData();

        // then
        assertEquals(Status.PUBLISHED, responseCouponEntity.getStatus());
        assertEquals("test-code", ((CouponEntity) apiResponse.getData()).getCode());
    }

    @Test
    void 존재하지_않는_쿠폰이거나_status가_USING이_아니어서_취소_실패() {
        // given
        given(couponRepository.findByCodeAndStatus(anyString(), any()))
                .willReturn(Optional.empty());

        // then
        assertThrows(RuntimeException.class, () -> {
            // when
            ApiResponse apiResponse = couponUpdateService.cancelCoupon("test-code");
        });
    }
}