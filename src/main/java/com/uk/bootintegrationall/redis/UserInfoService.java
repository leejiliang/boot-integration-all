package com.uk.bootintegrationall.redis;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @Description TODO
 */
@Service
public class UserInfoService {

    private static Logger logger = Logger.getLogger(UserInfoService.class.getName());

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

    @Cacheable(value = "user-info", key = "#userId")
    public UserInfo getUserInfo(String userId) {
        logger.info("penetration query db");
        var userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setName("test");
        userInfo.setAge(19);
        return userInfo;
    }

    @Cacheable(value = "car-info", key = "#userId")
    public UserInfo getCarInfo(String userId) {
        logger.info("penetration query db");
        var userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setName("test");
        userInfo.setAge(19);
        return userInfo;
    }

    @Cacheable(value = "banner-info", key = "#userId")
    public UserInfo getBannerInfo(String userId) {
        logger.info("penetration query db");
        var userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setName("test");
        userInfo.setAge(19);
        return userInfo;
    }

    @Cacheable(value = "bill-info", key = "#userId")
    public UserInfo getBillInfo(String userId) {
        logger.info("penetration query db");
        var userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setName("test");
        userInfo.setAge(19);
        return userInfo;
    }
}
