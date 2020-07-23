package com.kakaopay.coupon.api.persistence.repository;

import com.kakaopay.coupon.api.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findById(String id);
    boolean existsByNo(Long no);
    boolean existsById(String id);
}
