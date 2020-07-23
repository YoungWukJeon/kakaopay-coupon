package com.kakaopay.coupon.api.persistence.repository;

import com.kakaopay.coupon.api.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    private UserEntity savedUserEntity;

    @BeforeEach
    void setUp() throws Exception {
        savedUserEntity =
                UserEntity.builder()
                        .id("testid")
                        .password("testpass")
                        .build();
    }

    @Test
    void id가_존재하는지_확인() {
        testEntityManager.persistAndFlush(savedUserEntity);
        assertTrue(userRepository.existsById("testid"));
        assertFalse(userRepository.existsById("testid1"));
    }

    @Test
    void no가_존재하는지_확인() {
        testEntityManager.persistAndFlush(savedUserEntity);
        assertTrue(userRepository.existsByNo(1L));
        assertFalse(userRepository.existsByNo(2L));
    }
}