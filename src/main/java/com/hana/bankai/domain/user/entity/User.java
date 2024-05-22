package com.hana.bankai.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hana.bankai.domain.account.entity.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.*;

@Entity(name = "user") // entity 이름 정의
@Table(name = "user") // Database에 생성될 table의 이름 지정
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
@EntityListeners(AuditingEntityListener.class) // 이벤트가 발생되었을 때 자동 실행
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID userCode;

    @Column
    @NotNull
    private String userId;

    @Column
    @NotNull
    private String userPwd;

    @Column
    @NotNull
    private String userName;

    @Column(updatable = false)
    @NotNull
    private String userInherentNumber;

    @Column
    @NotNull
    private String userPhone;

    @Column
    @NotNull
    private String userAddr;

    @Column
    @NotNull
    private String userNameEn;

    @Column
    @NotNull
    private String userAddrDetail;

    @CreatedDate
    @Column(updatable = false) // 컬럼 수정 불가
    private LocalDate userCreatedAt;

    @Column
    @NotNull
    private String userEmail;

    @ColumnDefault("1000000")
    @Builder.Default()
    @Column
    private Long userTrsfLimit = 1000000L;

    @Column
    private String userMainAcc;

    @OneToMany(mappedBy = "user") // One(user) to Many(account)
    @JsonManagedReference // 순환 참조 해결
    private final List<Account> accountList = new ArrayList<>();
}
