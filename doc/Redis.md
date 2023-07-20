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