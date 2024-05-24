package com.hana.bankai.domain.user.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash(value = "token", timeToLive = 10)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class RefreshToken implements Serializable {
    @Id
    private String refreshToken;
    private String id;
}
