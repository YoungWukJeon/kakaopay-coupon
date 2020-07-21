package com.kakaopay.coupon.api.coupon.service;

import com.kakaopay.coupon.api.coupon.advice.exception.CouponCodeGenerationException;
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
import java.util.stream.IntStream;

@Service
public class CouponCreationService {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CodeGenerator codeGenerator;

    public static final int GENERATE_COUNT = 5; // 중복 코드가 발생했을 때 재시도 횟수

    @Transactional
    public CouponDto save() {
        return CouponDto.from(
                couponRepository.saveAndFlush(
                        CouponEntity.builder()
                                .code(generateCode().orElseThrow(CouponCodeGenerationException::new))
                                .build()));
    }

    @Transactional
    public List<CouponDto> saves(Integer count) {
        List<CouponEntity> couponEntities =
                IntStream.range(0, count)
                        .mapToObj(i -> generateCode().orElseThrow(CouponCodeGenerationException::new))
                        .map(s ->
                                CouponEntity.builder()
                                        .code(s)
                                        .build())
                        .collect(Collectors.toList());

        return couponRepository.saveAll(couponEntities)
                .stream()
                .map(CouponDto::from)
                .collect(Collectors.toList());
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
}
