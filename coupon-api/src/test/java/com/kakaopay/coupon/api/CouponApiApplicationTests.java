package com.kakaopay.coupon.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "local")
@SpringBootTest
class CouponApiApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("Boot Success!!");
	}

}
