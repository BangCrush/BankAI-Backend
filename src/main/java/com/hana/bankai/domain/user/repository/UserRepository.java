package com.hana.bankai.domain.user.repository;

import com.hana.bankai.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // 회원가입 여부 확인
    Boolean existsByUserInherentNumber(String userInherentNumber);

    // 중복 이메일 확인
    Boolean existsByUserEmail(String userEmail);

    // 중복 ID 확인
    Boolean existsByUserId(String userId);

    Optional<User> findByUserId(String userId);

    // 아이디 조회
    @Query("select u.userId from user u where u.userNameKr = :userNameKr and u.userEmail = :userEmail")
    Optional<String> findUserIdByUserNameKrAndUserEmail(String userNameKr, String userEmail);

    // 비밀번호 조회
    @Query("select u.userPwd from user u where u.userNameKr = :userNameKr and u.userId = :userId and u.userEmail = :userEmail")
    Optional<String> findUserPwdByUserNameKrAndUserIdAndUserEmail(String userNameKr, String userId, String userEmail);

    // 회원 정보 수정
    @Modifying(clearAutomatically = true) // bulk 연산 실행 후 1차 cache를 비워준다
                                          // 쿼리 수행 후 1차 cache와 DB의 동기화를 위해 추가
    @Query("update user u set u.userPwd = :userPwd, u.userEmail = :userEmail, u.userPhone = :userPhone, u.userAddr = :userAddr, u.userAddrDetail = :userAddrDetail, u.userMainAcc = :userMainAcc where u.userCode = :userCode")
    void updateUser(UUID userCode, String userPwd, String userEmail, String userPhone, String userAddr, String userAddrDetail, String userMainAcc);

}
