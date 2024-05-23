package com.hana.bankai.domain.accounthistory.entity;

import com.hana.bankai.domain.accounthistory.enumtype.HisType;
import com.hana.bankai.global.common.enumtype.BankCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity(name = "account_history") // entity 이름 정의
@Table(name = "account_history") // Database에 생성될 table의 이름 지정
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
@EntityListeners(AuditingEntityListener.class) // 이벤트가 발생되었을 때 자동 실행
public class AccountHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accHisCode;

    @Column
    @NotNull
    private Long hisAmount;

    @CreatedDate
    @Column(updatable = false) // 컬럼 수정 불가
    private LocalDate hisDate;

    @Column
    @NotNull
    private HisType hisType;

    @Column
    @NotNull
    private String inAccCode;

    @Column
    @NotNull
    private BankCode inBankCode;

    @Column
    @NotNull
    private String outAccCode;

    @Column
    @NotNull
    private BankCode outBankCode;

    @Column
    @NotNull
    private Long beforeInBal;

    @Column
    @NotNull
    private Long afterInBal;

    @Column
    @NotNull
    private Long beforeOutBal;

    @Column
    @NotNull
    private Long afterOutBal;
}
