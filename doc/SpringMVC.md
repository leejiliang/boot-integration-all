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
2. 通过DefaultFormattingConversionService注册日期和时间两个格式化工具类, 全局级别的.(无法覆盖到ExceptionHandler中的返回值)
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
### 3.2 返回值类型的统一封装
1. 定义统一返回值类型
参考类: 
```java
com.uk.bootintegrationall.springmvc.config.CResult
```
2. 封装返回值
```java
com.uk.bootintegrationall.springmvc.config.FastMvcResponseBodyAwareAdvice
```
### 3.3 全局异常处理
1. 主配置类
    `com.uk.bootintegrationall.springmvc.config.GlobalExceptionHandler`
2. 注意事项
    1. 通过`@ControllerAdvice`注解, 可以指定扫描范围, 例如: `@ControllerAdvice(basePackages = "com.uk.bootintegrationall.springmvc.controller")`
    2. 通过`@ExceptionHandler`注解, 可以指定处理的异常类型, 例如: `@ExceptionHandler(value = {Exception.class})`
    3. 通过`@ResponseBody`注解, 可以指定返回值类型, 例如: `@ResponseBody CResult`
    4. 通过`@ResponseStatus`注解, 可以指定返回的状态码, 例如: `@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)`
### 3.4 自定义参数注入
经典场景, 例如: 获取当前登录用户信息, 通过注解的方式, 注入到Controller中.
1. 定义用户信息解析器
   `com.uk.bootintegrationall.springmvc.config.UserInfoArgumentResolver`
2. 注册用户信息解析器
   `com.uk.bootintegrationall.springmvc.config.FastWebMvcConfigurer`
### 3.5 参数校验
To use the Java Validation API, we have to add a JSR 303 implementation, such as hibernate-validator:
```xml
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.0.10.Final</version>
</dependency>
```
使用校验逻辑: 
```java
@RestController
@RequestMapping("/car")
@Validated  // 启用参数校验
public class CarController {
    @GetMapping("/getCar5")
    public String getCar5(@RequestParam @Min(1970) @Max(value = 9999, message = "年份信息不合法") Integer year) {
        return year.toString();
    }
}
```
### 3.6 拦截器
### 3.7 过滤器
### 3.8 自定义404页面
### 3.9 自定义静态资源目录
### 3.10 自定义静态资源映射