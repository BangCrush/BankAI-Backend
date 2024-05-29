package com.hana.bankai.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hana.bankai.domain.account.entity.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "product") // entity 이름 정의
@Table(name = "product") // Database에 생성될 table의 이름 지정
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
@EntityListeners(AuditingEntityListener.class) // 이벤트가 발생되었을 때 자동 실행
public class Product {

    @Id
    private Long prodCode;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProdType prodType;

    @Column
    @NotNull
    private String prodName;

    @Column
    @NotNull
    private String prodDesc;

    @Column
    @NotNull
    private int joinPeriod;

    @Column
    @NotNull
    private double prodRate;

    @Column
    @NotNull
    private Long prodMin;

    @Column
    @NotNull
    private Long prodMax;

    @Column
    @NotNull
    private String joinMember;

    @Column
    @NotNull
    private Long prodLimit;

    @Column
    @NotNull
    private String prodRateMthd;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProdRepay prodRepay;


    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProdAcc prodAcc;

    @Column
    @NotNull
    private String prodPromo;

    @Column
    @NotNull
    private String prodTerms;

    @OneToMany(mappedBy = "product") // One(product) to Many(account)
    @JsonManagedReference // 순환 참조 해결
    private final List<Account> accountList = new ArrayList<>();

}
