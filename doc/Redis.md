# Boot整合Redis
## 1.引入依赖
```xml
```
## 2.配置
```yaml
spring.redis.url=redis://localhost:6379
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.username=
spring.redis.password=
spring.redis.client-type=lettuce
```
boot自动配置了RedisTemplate，可以直接使用.
```java
@AutoConfiguration
@ConditionalOnClass({RedisOperations.class})
@EnableConfigurationProperties({RedisProperties.class})
@Import({LettuceConnectionConfiguration.class, JedisConnectionConfiguration.class})
public class RedisAutoConfiguration {
    public RedisAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean(
        name = {"redisTemplate"}
    )
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }
}
```
## 3.简单使用示例
```java
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
    }
}
```
# 详细使用
## 1. 设置带过期时间的key
```java
redisTemplate.opsForValue().set("test-object", 1, 10, TimeUnit.SECONDS);
```
## 2. 模拟并发锁
如果业务处理时间过长, 锁自动释放, 可能会导致并发问题.
```java
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
```
## 3. 使用cacheable缓存方法的返回值
```java
@Cacheable(value = "user-info", key = "#userId")
    public UserInfo getUserInfo(String userId) {
        logger.info("penetration query db");
        var userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setName("test");
        userInfo.setAge(19);
        return userInfo;
    }
```
进行个性化配置
```java
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
        .withCacheConfiguration("user-info", RedisCacheConfiguration.defaultCacheConfig().prefixCacheNameWith("user-config::").entryTtl(Duration.of(100, ChronoUnit.SECONDS)))
        .withInitialCacheConfigurations(customCacheConfigurations())
        .build();
}

private Map<String, RedisCacheConfiguration> customCacheConfigurations() {
    return Map.of("car-info", RedisCacheConfiguration.defaultCacheConfig().prefixCacheNameWith("car-config::").entryTtl(Duration.of(20, ChronoUnit.SECONDS)),
        "banner-info", RedisCacheConfiguration.defaultCacheConfig().prefixCacheNameWith("banner-config::").entryTtl(Duration.of(40, ChronoUnit.SECONDS)));
}
```