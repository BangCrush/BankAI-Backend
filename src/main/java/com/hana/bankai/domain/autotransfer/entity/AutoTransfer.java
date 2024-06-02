package com.hana.bankai.domain.autotransfer.entity;

import com.hana.bankai.domain.account.entity.Account;
import com.hana.bankai.global.common.enumtype.BankCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "auto_transfer") // entity 이름 정의
@Table(name = "auto_transfer") // Database에 생성될 table의 이름 지정
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
@EntityListeners(AuditingEntityListener.class) // 이벤트가 발생되었을 때 자동 실행
public class AutoTransfer {

    @EmbeddedId // 복합키 설정(at_date, in_acc_code, out_acc_code)
    private AutoTransferId autoTransferId;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private BankCode inBankCode;

    @Column
    @NotNull
    private Long atAmount;

    @ManyToOne
    @MapsId("outAccCode")
    @JoinColumn(name = "out_acc_code")
    private Account account;
}

