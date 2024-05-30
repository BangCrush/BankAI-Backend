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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity(name = "user") // entity 이름 정의
@Table(name = "user") // Database에 생성될 table의 이름 지정
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
@EntityListeners(AuditingEntityListener.class) // 이벤트가 발생되었을 때 자동 실행
public class User implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID userCode;

    @Column(unique = true)
    @NotNull
    private String userId;

    @Column
    @NotNull
    private String userPwd;

    @Column
    @NotNull
    private String userNameKr;

    @Column
    @NotNull
    private String userNameEn;

    @Column(updatable = false, unique = true)
    @NotNull
    private String userInherentNumber;

    @Column(unique = true)
    @NotNull
    private String userEmail;

    @Column
    @NotNull
    private String userPhone;

    @Column
    @NotNull
    private String userAddr;

    @Column
    @NotNull
    private String userAddrDetail;

    @CreatedDate
    @Column(updatable = false) // 컬럼 수정 불가
    private LocalDate userCreatedAt;

    @Column
    private String userMainAcc;

    @OneToMany(mappedBy = "user") // One(user) to Many(account)
    @JsonManagedReference // 순환 참조 해결
    private final List<Account> accountList = new ArrayList<>();
  
    @OneToOne(mappedBy = "user") // cf."user_job"이 아니라 "user"
    private UserJob userJob;

    @OneToOne(mappedBy = "user")
    private UserTrsfLimit userTrsfLimit;

    // 권한 목록
    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    /* Override */

    // 해당 유저의 권한 목록
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() { return userId; }

    @Override
    public String getPassword() { return userPwd; }

    // 계정 만료 여부 (true: 만료 안 됨)
    @Override
    public boolean isAccountNonExpired() { return true; }

    // 계정 잠김 여부 (true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() { return true; }

    // 비밀번호 만료 여부 (true: 만료 안 됨)
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    // 사용자 활성화 여부 (true: 활성화)
    @Override
    public boolean isEnabled() { return true; }

}
