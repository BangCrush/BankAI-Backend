package com.hana.bankai.domain.user.repository;

import com.hana.bankai.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // 회원가입 여부 확인
    boolean existsByUserInherentNumber(String userInherentNumber);

    // 중복 이메일 확인
    boolean existsByUserEmail(String userEmail);

    // 중복 ID 확인
    boolean existsByUserId(String userId);

    // 아이디 찾기
    Optional<User> findByUserNameAndUserEmail(String userName, String userEmail);

    // 비밀번호 찾기
    Optional<User> findByUserNameAndUserIdAndUserEmail(String userName, String userId, String userEmail);
}
