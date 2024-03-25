package com.huanglb.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthTokenPool {
    public static final Long TIME_OUT = 24 * 3L;
    public static final TimeUnit TIME_UNIT = TimeUnit.HOURS;
    private final StringRedisTemplate stringRedisTemplate;

    public String makeToken(Long userId) {
        String token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(token, userId.toString(), TIME_OUT, TIME_UNIT);
        return token;
    }

    public Long validateToken(String token) {
        String userId = stringRedisTemplate.opsForValue().getAndExpire(token, TIME_OUT, TIME_UNIT);
        return userId == null ? null : Long.valueOf(userId);
    }

    public void deleteToken(String token) {
        stringRedisTemplate.delete(token);
    }
}
