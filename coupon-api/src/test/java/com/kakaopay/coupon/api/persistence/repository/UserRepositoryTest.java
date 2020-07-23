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
    void 존재하는_id로_조회() {
        testEntityManager.persistAndFlush(savedUserEntity);
        UserEntity userEntity = userRepository.findById("testid").orElseThrow(RuntimeException::new);
        assertEquals(savedUserEntity.getId(), userEntity.getId());
        assertEquals(savedUserEntity.getPassword(), userEntity.getPassword());
    }

    @Test
    void 존재하지_않는_id로_조회() {
        assertThrows(RuntimeException.class, () -> {
            userRepository.findById("testId").orElseThrow(RuntimeException::new);
        });
    }

    @Test
    void id가_존재하는지_확인() {
        testEntityManager.persistAndFlush(savedUserEntity);
        assertTrue(userRepository.existsById("testid"));
        assertFalse(userRepository.existsById("testid1"));
    }

    @Test
    void no가_존재하는지_확인() {
        long no = testEntityManager.persistAndGetId(savedUserEntity, Long.class);
        assertTrue(userRepository.existsByNo(no));
        assertFalse(userRepository.existsByNo(0L));
    }
}