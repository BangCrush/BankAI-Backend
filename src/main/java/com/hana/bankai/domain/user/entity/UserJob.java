package com.hana.bankai.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity(name = "user_job") // entity 이름 정의
@Table(name = "user_job") // Database에 생성될 table의 이름 지정
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString(exclude = "user") // user 정보를 출력할 때 무한루프 방지
@Getter
@EntityListeners(AuditingEntityListener.class) // 이벤트가 발생되었을 때 자동 실행
public class UserJob {

    @Id
    @Column(name = "user_code")
    private UUID userCode;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_code")
    private User user;

    @Column
    private String jobName;

    @Column
    private String companyName;

    @Column
    private String companyAddr;

    @Column
    private String companyPhone ;

}
