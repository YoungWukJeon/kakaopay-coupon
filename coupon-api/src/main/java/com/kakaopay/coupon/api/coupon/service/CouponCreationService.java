package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.common.ApiService;
import com.kakaopay.coupon.api.common.model.ApiResponse;
import com.kakaopay.coupon.api.coupon.model.CouponDto;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import com.kakaopay.coupon.api.persistence.repository.CouponRepository;
import com.kakaopay.coupon.api.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
public class CouponCreationService {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private ApiService apiService;
    @Autowired
    private CodeGenerator codeGenerator;

    public static final int GENERATE_COUNT = 5; // 중복 코드가 발생했을 때 재시도 횟수

    @Transactional
    public ApiResponse save() {
        String code = generateCode().orElseThrow();

        CouponEntity couponEntity =
                couponRepository.saveAndFlush(
                        CouponEntity.builder()
                                .code(code)
                                .build());

        return apiService.createSuccessResponse(
                convertCouponEntityToCouponResponse(couponEntity));
    }

    @Transactional
    public ApiResponse saves(Long count) {
        List<CouponEntity> couponEntities =
                LongStream.range(0, count)
                        .mapToObj(i -> generateCode().orElseThrow())
                        .map(s ->
                                CouponEntity.builder()
                                        .code(s)
                                        .build())
                        .collect(Collectors.toList());

        return apiService.createSuccessResponse(
                couponRepository.saveAll(couponEntities)
                        .stream()
                        .map(this::convertCouponEntityToCouponResponse)
                        .collect(Collectors.toList()));
    }

    // TODO: 2020-07-19 함수형 리팩토링 고려
    private Optional<String> generateCode() {
        for (int i = 0; i < GENERATE_COUNT; i++) {
            String code = codeGenerator.generate();
            if (!checkCodeExisted(code)) {
                return Optional.ofNullable(code);
            }
        }
        return Optional.empty();
    }

    private boolean checkCodeExisted(String code) {
        return couponRepository.findByCode(code).isPresent();
    }

    private CouponDto convertCouponEntityToCouponResponse(CouponEntity couponEntity) {
        return CouponDto.from(couponEntity);
    }
}
