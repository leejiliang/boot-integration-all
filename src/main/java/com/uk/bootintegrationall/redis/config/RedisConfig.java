package com.uk.bootintegrationall.redis.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 */
@Configuration
@EnableCaching
public class RedisConfig {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 配置序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheConfiguration redisCacheConfiguration = config
            // 键序列化方式 redis字符串序列化
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
            .prefixCacheNameWith("creams::")
            .entryTtl(Duration.of(60, ChronoUnit.SECONDS))
            // 值序列化方式 简单json序列化
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer));
        return RedisCacheManager.builder(factory)
            .cacheDefaults(redisCacheConfiguration)
            .withCacheConfiguration("user-info", redisCacheConfiguration.entryTtl(Duration.of(20, ChronoUnit.SECONDS)).disableKeyPrefix())
            .build();
    }


}

