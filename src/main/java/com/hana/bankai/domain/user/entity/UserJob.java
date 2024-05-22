package com.hana.bankai.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity(name = "user_job") // entity 이름 정의
@Table(name = "user_job") // Database에 생성될 table의 이름 지정
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
@EntityListeners(AuditingEntityListener.class) // 이벤트가 발생되었을 때 자동 실행
public class UserJob {

    @Id
    @OneToOne
    @JoinColumn(name = "user_code")
    private User user;

    @Column
    @NotNull
    private String jobName;

    @Column
    @NotNull
    private String companyName;

    @Column
    @NotNull
    private String companyAddr;

    @Column
    @NotNull
    private String companyPhone;

}
