package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.coupon.advice.exception.CouponNotFoundException;
import com.kakaopay.coupon.api.coupon.model.CouponDto;
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
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {
    @InjectMocks
    private CouponService couponService;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CouponEntity couponEntity;

    @Test
    public void 모든_쿠폰_조회() {
        // given
        given(couponRepository.findAll())
                .willReturn(Collections.emptyList());

        // when
        List<CouponDto> couponDtos = couponService.findAll();

        // then
        assertEquals(Collections.emptyList(), couponDtos);
    }

    @Test
    public void 존재하는_코드로_쿠폰_조회() {
        // given
        String code = anyString();
        given(couponRepository.findByCode(code))
                .willReturn(Optional.of(couponEntity));

        // when
        CouponDto couponDto = couponService.findByCode(code);

        // then
        assertEquals(couponEntity.getNo(), couponDto.getNo());
        assertEquals(couponEntity.getCode(), couponDto.getCode());
        assertEquals(couponEntity.getStatus(), couponDto.getStatus());
    }

    @Test
    public void 존재하지_않는_코드로_쿠폰_조회() {
        // given
        String code = anyString();
        given(couponRepository.findByCode(code))
                .willReturn(Optional.empty());

        // then
        assertThrows(CouponNotFoundException.class, () -> {
            // when
            couponService.findByCode(code);
        });
    }

    @Test
    void 존재하는_user로_쿠폰_조회() {
        // given
        Long userNo = anyLong();
        couponEntity.publishToUser(userNo);
        List<CouponEntity> couponEntities = List.of(couponEntity);

        given(couponRepository.findAllByUserNo(anyLong()))
                .willReturn(couponEntities);

        // when
        List<CouponDto> couponDtos = couponService.findAllByUserNo(userNo);

        // then
        assertEquals(couponEntities.size(), couponDtos.size());
        IntStream.range(0, couponEntities.size())
                .forEach(i -> {
                    CouponEntity couponEntity = couponEntities.get(i);
                    CouponDto couponDto = couponDtos.get(i);
                    assertEquals(couponEntity.getNo(), couponDto.getNo());
                    assertEquals(couponEntity.getCode(), couponDto.getCode());
                    assertEquals(couponEntity.getStatus(), couponDto.getStatus());
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
        List<CouponEntity> couponEntities = List.of(couponEntity);

        given(couponRepository.findAllByStatusAndExpirationDateBetween(any(), any(), any()))
                .willReturn(List.of(couponEntity));

        // when
        List<CouponDto> couponDtos = couponService.findAllByExpirationDateBetweenToday(dateTime);

        // then
        assertEquals(couponEntities.size(), couponDtos.size());
        IntStream.range(0, couponEntities.size())
                .forEach(i -> {
                    CouponEntity couponEntity = couponEntities.get(i);
                    CouponDto couponDto = couponDtos.get(i);
                    assertEquals(couponEntity.getNo(), couponDto.getNo());
                    assertEquals(couponEntity.getCode(), couponDto.getCode());
                    assertEquals(couponEntity.getStatus(), couponDto.getStatus());
                });
    }
}