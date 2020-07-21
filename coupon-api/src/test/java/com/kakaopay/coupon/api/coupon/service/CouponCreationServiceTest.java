package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.coupon.model.CouponDto;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import com.kakaopay.coupon.api.persistence.repository.CouponRepository;
import com.kakaopay.coupon.api.util.CodeGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CouponCreationServiceTest {
    @InjectMocks
    private CouponCreationService couponCreationService;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CodeGenerator codeGenerator;

    @Test
    void 단건_저장_성공() {
        // given
        CouponEntity couponEntity =
                CouponEntity.builder()
                        .code("test-code")
                        .build();

        given(codeGenerator.generate())
                .willReturn("test-code");
        given(couponRepository.findByCode(anyString()))
                .willReturn(Optional.empty());
        given(couponRepository.saveAndFlush(any()))
                .willReturn(couponEntity);

        // when
        CouponDto couponDto = couponCreationService.save();

        // then
        assertEquals(couponEntity.getCode(), couponDto.getCode());
    }

    @Test
    void 단건_저장_실패(){
        // given
        CouponEntity couponEntity =
                CouponEntity.builder()
                        .code("test-code")
                        .build();

        given(codeGenerator.generate())
                .willReturn("test-code");
        given(couponRepository.findByCode(anyString()))
                .willReturn(Optional.ofNullable(couponEntity));

        // then
        assertThrows(RuntimeException.class, () -> {
            // when
            couponCreationService.save();
        });
    }

    @Test
    void 다수_저장_성공() {
        // given
        int count = 5;

        List<CouponEntity> couponEntities =
                IntStream.range(0, count)
                        .mapToObj(i ->
                                CouponEntity.builder()
                                        .code("test-code" + i)
                                        .build())
                        .collect(Collectors.toList());

        given(codeGenerator.generate())
                .willReturn("test-code");
        given(couponRepository.findByCode(anyString()))
                .willReturn(Optional.empty());
        given(couponRepository.saveAll(anyCollection()))
                .willReturn(couponEntities);

        // when
        List<CouponDto> couponDtos = couponCreationService.saves(count);

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
    void 코드_생성_성공() {
        // given
        given(codeGenerator.generate())
                .willReturn("test-code");
        given(couponRepository.findByCode(anyString()))
                .willReturn(Optional.empty());

        // when
        Optional<String> code = ReflectionTestUtils.invokeMethod(couponCreationService, "generateCode");

        // then
        assertTrue(code.isPresent());
    }

    @Test
    void 코드_생성_실패() {
        // given
        given(codeGenerator.generate())
                .willReturn("test-code");
        given(couponRepository.findByCode(anyString()))
                .willReturn(
                        Optional.of(
                                CouponEntity.builder()
                                        .code("test-code")
                                        .build()));

        // when
        Optional<String> code = ReflectionTestUtils.invokeMethod(couponCreationService, "generateCode");

        // then
        assertTrue(code.isEmpty());
        verify(codeGenerator, times(5)).generate();
    }

    @Test
    void 존재하는_코드_중복_체크() {
        // given
        String code = anyString();
        given(couponRepository.findByCode(code))
                .willReturn(
                        Optional.of(
                                CouponEntity.builder()
                                        .code("test-code")
                                        .build()));

        // when
        Boolean result = ReflectionTestUtils.invokeMethod(couponCreationService, "checkCodeExisted", "test-code");

        // then
        assertTrue(result);
    }

    @Test
    void 존재하지_않는_코드_중복_체크() {
        // given
        String code = anyString();
        given(couponRepository.findByCode(code))
                .willReturn(Optional.empty());

        // when
        Boolean result = ReflectionTestUtils.invokeMethod(couponCreationService, "checkCodeExisted", "test-code");

        // then
        assertFalse(result);
    }
}