package com.hana.bankai.domain.user.service;

import com.hana.bankai.domain.user.dto.RefreshToken;
import com.hana.bankai.domain.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final RefreshTokenRepository userRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final String TOKEN_KEY = "token";



    public RefreshToken save(RefreshToken refreshToken) {
        log.info(">>>>>>>>"+refreshToken.toString());
        redisTemplate.opsForHash().put(TOKEN_KEY, refreshToken.getRefreshToken(), refreshToken.getId());
        return refreshToken;

//        return userRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findById(String id) {
        return userRepository.findById(id);
    }

}
