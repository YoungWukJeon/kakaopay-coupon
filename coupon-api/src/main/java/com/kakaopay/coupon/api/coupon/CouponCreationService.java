package com.kakaopay.coupon.api.coupon;

import com.kakaopay.coupon.api.common.ApiService;
import com.kakaopay.coupon.api.common.model.ApiResponse;
import com.kakaopay.coupon.api.coupon.model.response.CouponResponse;
import com.kakaopay.coupon.api.persistence.entity.CouponEntity;
import com.kakaopay.coupon.api.persistence.repository.CouponRepository;
import com.kakaopay.coupon.api.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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

    private CouponResponse convertCouponEntityToCouponResponse(CouponEntity couponEntity) {
        return CouponResponse.from(
                couponEntity.getNo(), couponEntity.getCode(),
                couponEntity.getCreatedDate(), couponEntity.getPublishedDate(), couponEntity.getExpirationDate(),
                couponEntity.getStatus(), couponEntity.getUserNo());
    }
}
