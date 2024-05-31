package com.hana.bankai.domain.user.repository;

import com.hana.bankai.domain.user.entity.UserTrsfLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserTrsfLimitRepository extends JpaRepository<UserTrsfLimit, UUID> {
    // 일일 한도 변경
    @Modifying(clearAutomatically = true) // bulk 연산 실행 후 1차 cache를 비워준다
                                          // 쿼리 수행 후 1차 cache와 DB의 동기화를 위해 추가
    @Query("update user_trsf_limit utl set utl.dailyLimit = :dailyLimit where utl.userCode = :userCode")
    void updateDailyLimit(UUID userCode, Long dailyLimit);
}
