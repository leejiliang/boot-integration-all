package com.uk.bootintegrationall.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 */
@Service
public class UserInfoService {

    private final RedisTemplate<Object, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    public UserInfoService(RedisTemplate<Object, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void testRedis() {
        redisTemplate.opsForValue().set("test-object", 1);
        stringRedisTemplate.opsForValue().set("test-string", "1");
        stringRedisTemplate.opsForValue().set("test-string-exp", "1", 10, TimeUnit.SECONDS);
    }
}
