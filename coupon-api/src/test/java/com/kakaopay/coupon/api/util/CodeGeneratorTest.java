package com.kakaopay.coupon.api.util;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class CodeGeneratorTest {
    @Test
    void 랜덤_쿠폰_코드_생성() {
        CodeGenerator codeGenerator = new CodeGenerator();
        String code = codeGenerator.generate();
        System.out.println("Generated Code: " + code);
        assertTrue(code.matches(CodeGenerator.CODE_PATTERN));
    }
}