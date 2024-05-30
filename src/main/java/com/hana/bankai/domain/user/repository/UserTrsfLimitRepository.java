package com.hana.bankai.domain.user.repository;

import com.hana.bankai.domain.user.entity.UserTrsfLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserTrsfLimitRepository extends JpaRepository<UserTrsfLimit, UUID> {
}
