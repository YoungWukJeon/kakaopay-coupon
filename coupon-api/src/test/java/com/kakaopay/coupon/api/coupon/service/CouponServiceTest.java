package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.common.ApiService;
import com.kakaopay.coupon.api.common.model.ApiResponse;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import com.kakaopay.coupon.api.persistence.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {
    @InjectMocks
    private CouponService couponService;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private ApiService apiService;
    @Mock
    private CouponEntity couponEntity;

    @Test
    public void 모든_쿠폰_조회() {
        // given
        given(couponRepository.findAll())
                .willReturn(Collections.emptyList());
        given(apiService.createSuccessResponse(anyCollection()))
                .willReturn(ApiResponse.from(200, "성공", Collections.emptyList()));

        // when
        ApiResponse apiResponse = couponService.findAll();

        // then
        assertEquals(200, apiResponse.getStatus());
        assertEquals("성공", apiResponse.getMessage());
        assertEquals(Collections.emptyList(), apiResponse.getData());
    }

    @Test
    public void 존재하는_코드로_쿠폰_조회() {
        // given
        String code = anyString();
        given(couponRepository.findByCode(code))
                .willReturn(Optional.of(couponEntity));
        given(apiService.createSuccessResponse(any()))
                .willReturn(ApiResponse.from(200, "성공", couponEntity));

        // when
        ApiResponse apiResponse = couponService.findByCode(code);

        // then
        assertEquals(200, apiResponse.getStatus());
        assertEquals("성공", apiResponse.getMessage());
        assertEquals(couponEntity, apiResponse.getData());
    }

    @Test
    public void 존재하지_않는_코드로_쿠폰_조회() {
        // given
        String code = anyString();
        given(couponRepository.findByCode(code))
                .willReturn(Optional.empty());

        // then
        assertThrows(RuntimeException.class, () -> {
            // when
            ApiResponse apiResponse = couponService.findByCode(code);
        });
    }

    @Test
    void 존재하는_user로_쿠폰_조회() {
        // given
        Long userNo = anyLong();
        couponEntity.publishToUser(userNo);
        given(couponRepository.findByUserNo(userNo))
                .willReturn(Optional.of(couponEntity));
        given(apiService.createSuccessResponse(any()))
                .willReturn(ApiResponse.from(200, "성공", couponEntity));

        // when
        ApiResponse apiResponse = couponService.findByUserNo(userNo);

        // then
        assertEquals(200, apiResponse.getStatus());
        assertEquals("성공", apiResponse.getMessage());
        assertEquals(couponEntity, apiResponse.getData());
    }

    @Test
    void 존재하지_않는_user로_쿠폰_조회() {
        // given
        Long userNo = anyLong();
        couponEntity.publishToUser(userNo);
        given(couponRepository.findByUserNo(userNo))
                .willReturn(Optional.empty());

        // then
        assertThrows(RuntimeException.class, () -> {
            // when
            ApiResponse apiResponse = couponService.findByUserNo(userNo);
        });
    }

    @Test
    void localDateTime에서_날짜만_넣고_시간은_0으로_변경() {
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime date = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), 0, 0, 0);
        LocalDateTime comparingDate = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), 0, 0, 1);
        System.out.println(dateTime);
        System.out.println(date);
        System.out.println(comparingDate);
        assertEquals(-1, date.compareTo(comparingDate));
    }

    @Test
    void 오늘_만료된_쿠폰_전체_조회() {
        // given
        LocalDateTime dateTime = LocalDateTime.now();
        couponEntity.publishToUser(anyLong());
        couponEntity.expireCoupon();

        given(couponRepository.findAllByStatusAndExpirationDateBetween(any(), any(), any()))
                .willReturn(List.of(couponEntity));
        given(apiService.createSuccessResponse(anyCollection()))
                .willReturn(ApiResponse.from(200, "성공", List.of(couponEntity)));

        // when
        ApiResponse apiResponse = couponService.findAllByExpirationDateBetweenToday(dateTime);

        // then
        assertIterableEquals(List.of(couponEntity), (List<CouponEntity>) apiResponse.getData());
    }
}