package com.kakaopay.coupon.api.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class CodeGenerator {
    public static final String CODE_FORMAT = "%4s-%4s-%4s-%4s-%4s";
    public static final String CODE_PATTERN = "[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}";

    public String generate() {
        String str1 = RandomStringUtils.randomAlphanumeric(4);
        String str2 = RandomStringUtils.randomAlphanumeric(4);
        String str3 = RandomStringUtils.randomAlphanumeric(4);
        String str4 = RandomStringUtils.randomAlphanumeric(4);
        String str5 = RandomStringUtils.randomAlphanumeric(4);

        return String.format(CODE_FORMAT, str1, str2, str3, str4, str5);
    }
}
