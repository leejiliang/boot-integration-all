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
        var absent = redisTemplate.opsForValue().setIfAbsent("con-lock", "1", 10, TimeUnit.SECONDS);
        if(!absent){
            System.out.println("获取锁失败");
            return;
        }
        try {
            System.out.println("处理并发业务");
        }finally {
            redisTemplate.delete("con-lock");
        }
    }
}
