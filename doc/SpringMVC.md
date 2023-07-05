# SpringBoot整合SpringMVC
## 1. 添加依赖
pom.xml
```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
```
## 2. 基础配置
```properties
# 端口号
    server.port=8080
    # 项目根路径
    server.servlet.context-path=/api
```
## 3. 常用配置
### 3.1 入参类型转换
#### 3.1.1 日期类型转换
1. 通过@DateTimeFormat(pattern = "yyyy-MM-dd")解决, 方法级别的.
```java
    @GetMapping("/getCar")
    public String getCar(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate produceDate) {
        System.out.println("produceDate = " + produceDate);
        return "car";
    }
```
2. 通过DefaultFormattingConversionService注册日期和时间两个格式化工具类, 全局级别的.
```java
@Configuration
public class DateTimeConfig extends WebMvcConfigurationSupport {
    @Bean
    @Override
    public FormattingConversionService mvcConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

        DateTimeFormatterRegistrar dateTimeRegistrar = new DateTimeFormatterRegistrar();
        dateTimeRegistrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dateTimeRegistrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        dateTimeRegistrar.registerFormatters(conversionService);

        DateFormatterRegistrar dateRegistrar = new DateFormatterRegistrar();
        dateRegistrar.setFormatter(new DateFormatter("yyyy-MM-dd"));
        dateRegistrar.registerFormatters(conversionService);

        return conversionService;
    }
}
```
3. 通过配置文件来解决
```properties
#spring.mvc.format.date=iso
#spring.mvc.format.date-time=iso
#spring.mvc.format.time=iso

spring.mvc.format.date=yyyy-MM-dd
spring.mvc.format.date-time=yyyy-MM-dd HH:mm:ss
spring.mvc.format.time=HH:mm:ss
```