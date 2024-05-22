package com.hana.bankai.domain.account.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hana.bankai.domain.product.entity.Product;
import com.hana.bankai.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity(name = "account") // entity 이름 정의
@Table(name = "account") // Database에 생성될 table의 이름 지정
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
@EntityListeners(AuditingEntityListener.class) // 이벤트가 발생되었을 때 자동 실행
public class Account {

    @Id
    @GeneratedValue(generator = "acc-code-generator")
    @GenericGenerator(name = "acc-code-generator", strategy = "com.hana.bankai.domain.account.entity.AccCodeGenerator")
    private String accCode;

    @ManyToOne
    @JoinColumn(name = "prod_code")
    @JsonBackReference
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_code")
    @JsonBackReference
    private User user;

    @Column
    @NotNull
    private int accTime;

    @Column
    @ColumnDefault("0")
    @Builder.Default()
    private Long accBalance = 0L;

    @CreatedDate
    @Column(updatable = false) // 컬럼 수정 불가
    private LocalDate accJoinDate;

    @Column
    @ColumnDefault("500000")
    @Builder.Default()
    private Long accTrsfLimit = 500000L;

    @Column
    @NotNull
    private String accPw;

    @Column
    @Builder.Default()
    private AccStatus status = AccStatus.valueOf("ACTIVE");

}
