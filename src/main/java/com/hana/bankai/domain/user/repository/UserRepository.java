package com.hana.bankai.domain.user.repository;

import com.hana.bankai.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // 로그인 - 입력받은 아이디 기준으로 계정 조회
    Optional<User> findByUserId(String userId);

    // 회원가입 여부 확인
    Boolean existsByUserInherentNumber(String userInherentNumber);

    // 중복 이메일 확인
    Boolean existsByUserEmail(String userEmail);

    // 중복 ID 확인
    Boolean existsByUserId(String userId);

    // 아이디 조회
    @Query("select u.userId from user u where u.userNameKr = :userNameKr and u.userEmail = :userEmail")
    Optional<String> findUserIdByUserNameKrAndUserEmail(String userNameKr, String userEmail);

    // 비밀번호 조회
    @Query("select u.userPwd from user u where u.userNameKr = :userNameKr and u.userId = :userId and u.userEmail = :userEmail")
    Optional<String> findUserPwdByUserNameKrAndUserIdAndUserEmail(String userNameKr, String userId, String userEmail);

}
