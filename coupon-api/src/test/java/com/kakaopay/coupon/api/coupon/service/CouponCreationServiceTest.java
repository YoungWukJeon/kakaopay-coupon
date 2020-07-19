package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.common.ApiService;
import com.kakaopay.coupon.api.common.model.ApiResponse;
import com.kakaopay.coupon.api.coupon.service.CouponCreationService;
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
import java.util.stream.LongStream;

import static org.mockito.BDDMockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CouponCreationServiceTest {
    @InjectMocks
    private CouponCreationService couponCreationService;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private ApiService apiService;
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
        given(apiService.createSuccessResponse(any()))
                .willReturn(ApiResponse.from(200, "성공", couponEntity));

        // when
        ApiResponse apiResponse = couponCreationService.save();

        // then
        assertEquals(200, apiResponse.getStatus());
        assertEquals("성공", apiResponse.getMessage());
        assertEquals(couponEntity.getCode(), ((CouponEntity) apiResponse.getData()).getCode());
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
            ApiResponse apiResponse = couponCreationService.save();
        });
    }

    @Test
    void 다수_저장_성공() {
        // given
        long count = 5L;

        List<CouponEntity> couponEntities =
                LongStream.range(0, count)
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
        given(apiService.createSuccessResponse(anyCollection()))
                .willReturn(ApiResponse.from(200, "성공", couponEntities));

        // when
        ApiResponse apiResponse = couponCreationService.saves(count);

        // then
        assertEquals(200, apiResponse.getStatus());
        assertEquals("성공", apiResponse.getMessage());
        assertIterableEquals(couponEntities, ((List<CouponEntity>) apiResponse.getData()));
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