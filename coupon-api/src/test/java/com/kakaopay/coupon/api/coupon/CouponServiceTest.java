package com.kakaopay.coupon.api.coupon;

import com.kakaopay.coupon.api.common.ApiService;
import com.kakaopay.coupon.api.common.model.ApiResponse;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import com.kakaopay.coupon.api.persistence.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
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
}