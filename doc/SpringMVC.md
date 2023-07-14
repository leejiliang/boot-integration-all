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
1. 使用内置校验逻辑: 
```java
@RestController
@RequestMapping("/car")
@Validated  // 启用参数校验
public class CarController {
    @GetMapping("/getCar5")
    public String getCar5(@RequestParam @Min(1970) @Max(value = 9999, message = "年份信息不合法") Integer year) {
        return year.toString();
    }
    
   @PostMapping("/getCar6")
   public String getCar5(@RequestBody @Valid CarReq carReq) {
      return carReq.toString();
   }
}

public class CarReq {
    private String carName;
    private String carColor;
    @Max(value = 9999999, message = "价格不能超过9999999")
    private BigDecimal carPrice;
}
```
2. 自定义参数校验逻辑

- 定义校验注解: `com.uk.bootintegrationall.springmvc.annotation.CarName`
- 自定义校验逻辑: `com.uk.bootintegrationall.springmvc.validator.CarNameValidator`
### 3.6 拦截器
HandlerInterceptor与WebRequestInterceptor
WebRequestInterceptor间接实现了HandlerInterceptor，只是他们之间使用WebRequestHandlerInterceptorAdapter适配器类联系。
不同点
WebRequestInterceptor的入参WebRequest是包装了HttpServletRequest 和HttpServletResponse的，通过WebRequest获取Request中的信息更简便。
2.WebRequestInterceptor的preHandle是没有返回值的，说明该方法中的逻辑并不影响后续的方法执行，所以这个接口实现就是为了获取Request中的信息，或者预设一些参数供后续流程使用。
3.HandlerInterceptor的功能更强大也更基础，可以在preHandle方法中就直接拒绝请求进入controller方法。
使用场景
这个在上条已经说了，如果想更方便获取HttpServletRequest的信息就使用WebRequestInterceptor，当然这些HandlerInterceptor都能做，只不过要多写点代码

如何配置
配置类继承WebMvcConfigurationSupport或WebMvcConfigurerAdapter类，重写addInterceptors，InterceptorRegistry实例就可以直接添加。
顺便说下继承WebMvcConfigurationSupport或WebMvcConfigurerAdapter的区别，继承WebMvcConfigurationSupport不需要声明@EnableWebMvc注解，继承WebMvcConfigurerAdapter需要
4. 实践
4.1 定义拦截器
`com.uk.bootintegrationall.springmvc.interceptor.AuthInterceptor`
`com.uk.bootintegrationall.springmvc.interceptor.LoggerInterceptor`
4.2 注册拦截器
`com.uk.bootintegrationall.springmvc.config.FastWebMvcConfigurer#addInterceptors`

### 3.7 过滤器
唯一的作用就是过滤，它不仅可以过滤请求，还可以过滤响应，当请求到达 Servlet 容器，会先经过 Filter ，然后再交给 Servlet，之后 Filter 还可以对 Servlet 的响应进一步处理。并且多个 Filter 还能形成一个链。
![](https://image-blog-lee.oss-cn-hangzhou.aliyuncs.com/uPic/J4qDa8.jpg)
Filter接口
```java
public interface Filter {

    public void init(FilterConfig filterConfig) throws ServletException;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;

    public void destroy();
}
```
Filter 中定义了三个方法。
- init：这是 Filter 的初始化方法，这个方法只会被容器调用一次，方法参数 FilterConfig 表示 Filter 的配置，可以利用这个参数读取初始化参数、ServletContext 等。

- doFilter：这是 Filter 处理请求的核心方法，当请求到达时容器先回调这个方法处理请求，除了 request、response，这个方法还可以拿到过滤器链 FilterChain 对象，只有调用了 FilterChain#doFilter方法，容器才会使用过滤器链中的下一个过滤器处理请求，如果当前 Filter 已经是链中的最后一个，则会交给 Servlet 处理。

- destroy：容器停止时回调的方法，用于做一些资源清理的工作。

Spring MVC 内置 Filter
针对一些通用的场景，Spring MVC 内置了一些 Filter，下面看常用的有哪些。
CharacterEncodingFilter：用于设置请求体、响应体字符集的过滤器，使用这个过滤器可以统一字符编码，避免出现乱码现象。
CorsFilter：这是用来处理跨域的过滤器，请求到达这个过滤器时，会根据配置添加跨域相关的响应头。
FormContentFilter：对于请求方法为PUT、PATCH、DELETE，内容类型为表单application/x-www-form-urlencoded的请求，请求体中的参数无法通过 ServletRequest#getParameter 方法读取，这个过滤器对请求已经包装，以便可以通过 #getParameter 方法读取参数。
Spring MVC 中的 Filter 配置
自从 Spring MVC 提供拦截器 HandlerInterceptor 之后，过滤器 Filter 的一部分功能已经可以搬到拦截器了，但有时还是会不可避免的使用到过滤器，如跨域处理。因此需要自定义过滤器 Filter，并配置到 Servlet 容器中，Spring MVC 在不同的阶段也提供了不同的配置方案，具体来说主要有 6 种。
#### 1. 配置文件 web.xml 配置
   Spring MVC 基于 Servlet 规范，Spring 早期，Servlet 和 Filter 配置方式与传统的 Java Web 项目并没有任何区别，需要在 web.xml 配置 Filter 清单。示例如下。
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
          http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

   <filter>
      <filter-name>cors</filter-name>
      <filter-class>com.zzuhkp.mvc.CorsFilter</filter-class>
      <init-param>
         <param-name>allowedMethods</param-name>
         <param-value>GET,POST</param-value>
      </init-param>
   </filter>

   <filter-mapping>
      <filter-name>cors</filter-name>
      <url-pattern>/*</url-pattern>
   </filter-mapping>

</web-app>
```
#### 2. @WebFilter 注解配置
   Java 5 注解诞生后，Servlet 在 3.0 新引入了 @WebFilter 注解，用来替代 web.xml 文件中 Filter 的配置。Servlet 容器启动后会扫描类路径下的文件，遇到携带 @WebFilter 的注解后就会将这个类注册到容器中。因此在 Spring MVC 环境下也可以直接使用这个注解，和 xml 配置等同的注解配置如下。
   ```java
   @WebFilter(urlPatterns = "/*", initParams = {@WebInitParam(name = "allowedMethods", value = "GET,POST")})
   public class CorsFilter implements Filter {
      @Override
      public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
      }
   }
   ```
注意: boot环境需要搭配注解: @ServletComponentScan 使用, 否则不生效.
#### 3. ServletContainerInitializer 配置
   除了常规的 Servlet 规范中的 xml 和 @WebFilter 配置方式， Servlet 3.0 规范还提供了一个 ServletContainerInitializer 接口，Servlet 容器启动后会扫描类路径，标注了 @HandlesTypes 注解的 ServletContainerInitializer 接口实现将会被回调。因此，在 Spring MVC 中也可以利用这个特性添加 Filter，具体代码如下。
   ```java
   @HandlesTypes({})
   public class FilterInitializer implements ServletContainerInitializer {
      @Override
      public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
         FilterRegistration.Dynamic dynamic = ctx.addFilter("cors", new CorsFilter());
         dynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
         dynamic.setInitParameter("allowedMethods","GET,POST");
      }
   }
   ```
#### 4. WebApplicationInitializer 配置
   Spring 3.1 版本利用了上述 Servlet 规范中 ServletContainerInitializer 的特性，提供了这个接口的实现 SpringServletContainerInitializer，并在实现中回调了 Spring 提供的 WebApplicationInitializer 接口。因此，Spring MVC 环境也可以直接实现 WebApplicationInitializer 来手动配置 Filter。注意：只需要实现接口，无需特定配置，Servlet 容器会把这个类告诉 SpringServletContainerInitializer。示例代码如下。
   ```java
   public class CorsWebApplicationInitializer implements WebApplicationInitializer {
      @Override
      public void onStartup(ServletContext servletContext) throws ServletException {
         FilterRegistration.Dynamic dynamic = servletContext.addFilter("cors", new CorsFilter());
         dynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
         dynamic.setInitParameter("allowedMethods", "GET,POST");
      }
   }
   ```
#### 5. Spring Bean 配置
   除了普通 Spring MVC 环境下的配置，Spring Boot 环境中，Spring Boot 1.4 及之后版本下还可以直接将 Filter 注册为 Bean，Filter Bean 将应用到所有的请求中。示例代码如下。
   ```java
   @Component
   public class CorsFilter implements Filter {
      @Override
      public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
      }
   }
   ```
#### 6. FilterRegistrationBean 配置
   FilterRegistrationBean 同样是 Spring Boot 1.4 版本提出的一个新类型，这个类允许指定过滤的请求路径，将这个类配置为 Bean 即可。示例代码如下。
   ```java
   @Configuration
   public class MvcConfig {
      @Bean
      public FilterRegistrationBean<CorsFilter> filterRegistrationBean() {
         FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter());
         bean.addUrlPatterns("/*");
         bean.addInitParameter("allowedMethods", "GET,POST");
         return bean;
      }
   }
   ```
   总结
   本文主要对 Filter 的概念做了简单介绍，并介绍了几种 Spring MVC 内置的 Filter，最后介绍了 6 种在 Spring MVC 配置 Filter 的方式，其中 web.xml、@WebFilter、ServletContainerInitializer、WebApplicationInitializer 这 4 种配置方式适用于普通的 Spring MVC 环境，Filter Bean 配置、FilterRegistrationBean Bean 配置 适用于 Spring Boot 环境下的 Spring MVC，读者需要加以留意。

- 实践:
   - 定义filter
`com.uk.bootintegrationall.springmvc.filter.LoggerFilter`
   - 配置filter
      启动类添加注解: @ServletComponentScan
### 3.8 自定义404页面
1. 关闭spring提供的whitelabel错误页面
   ```properties
   server.error.whitelabel.enabled=false
   ```
2. 通过spring-boot-starter-thymeleaf发现自定义404页面
   - 在resource/templates目录下创建error目录
   - 在error目录下创建404.html
   - 页面内容自定义
   - 测试访问: http://localhost:8080/abc

### 3.9 自定义静态资源目录
参考文档: [Serve Static Resources with Spring](https://www.baeldung.com/spring-mvc-static-resources)
SpringBoot通过ResourceHttpRequestHandler 类来配置静态资源目录
默认的静态资源目录: /static, /public, /resources, and /META-INF/resources
将静态资源放入以上目录下即可直接访问
以上都是默认的静态资源目录, 如果想自定义静态资源目录, 可以通过配置文件来实现
```properties
## 所有的静态资源都必须添加前缀/content/xxx.html
spring.mvc.static-path-pattern=/content/**
## 指定新的静态资源目录, 默认的静态资源目录将失效
spring.web.resources.static-locations=classpath:/files/,classpath:/static-files
```
### 3.10 跨域配置
[CORS With Spring](https://www.baeldung.com/spring-cors)

基于安全的考虑，W3C规范规定浏览器禁止访问不同域(origin)的资源，目前绝大部分浏览器遵循这一规范，从而衍生出了跨域资源共享 (CORS)问题，相比于IFRAME或JSONP，CORS更全面并且更安全，Spring Mvc为我们提供了一套多粒度的CORS解决方案。

CORS的工作原理是添加新的HTTP headers来让服务器描述哪些源的请求可以访问该资源，对于可能对服务器造成不好影响的请求，规范规定浏览器需要先发送“预检”请求（也就是OPTION请求），在预检请求通过后再发送实际的请求，服务器还可以通知客户端是否应该随请求发送“凭据”（例如 Cookie 和 HTTP 身份验证）

复现跨域问题, 启动项目在两个端口, 8080, 9090 , 工程提供了两个配置, 通过profile可以分别在两个端口启动连个服务, 
在页面http://localhost:8080/index.html 中访问 localhost:9090/api/car/getCar7, 会发现跨域问题.
解决方式
1. 通过注解@CrossOrigin, 可以添加在类上, 也可以添加在方法上
   ```java
   @CrossOrigin(origins = "http://localhost:8080")
   @GetMapping("/getCar7")
   public Car getCar7() {
      return new Car("bmw", 1000000, new Date());
   }
   ```
2. 通过重写WebMvcConfigurer的addCorsMappings方法
   ```java
   @Configuration
   public class MvcConfig implements WebMvcConfigurer {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
         registry.addMapping("/api/**")
                 .allowedOrigins("http://localhost:8080")
                 .allowedMethods("GET", "POST")
                 .allowedHeaders("*")
                 .allowCredentials(true)
                 .maxAge(3600);
      }
   }
   ```
3. CORS With Spring Security

If we use Spring Security in our project, we must take an extra step to make sure it plays well with CORS. That's because CORS needs to be processed first. Otherwise, Spring Security will reject the request before it reaches Spring MVC.
Luckily, Spring Security provides an out-of-the-box solution:

@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and()...
    }
}

### 3.11 文件上传
通过配置, 进行一些通用配置
```properties
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
spring.servlet.multipart.location=classpath:/public/
```
1. 通过MultipartFile接收文件
```java
@PostMapping("/upload/image")
    public void uploadCarImage(@RequestParam MultipartFile file, ModelMap modelMap) {
        modelMap.addAttribute("file", file);
        storageService.store(file);
    }
```
2. 定义文件上传表单
```html
<form method="POST" action="/api/car/upload/image" enctype="multipart/form-data">
            <table>
                <tr>
                    <td><label path="file">Select a file to upload</label></td>
                    <td><input type="file" name="file" /></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Submit" /></td>
                </tr>
            </table>
            </form>
```
3. 定义文件的保存服务
`com.uk.bootintegrationall.springmvc.StorageService`


### 3.12 响应图片/视屏数据
[参考文档](https://www.baeldung.com/spring-mvc-image-media-data)
1. Using the HttpServletResponse
直接把文件通过response输出
```java
@GetMapping("/image")
public void getImageAsByteArray(HttpServletResponse response) throws IOException {
    InputStream inputStream= servletContext.getResourceAsStream("/WEB-INF/images/image-example.jpg");
    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    IOUtils.copy(inputStream, response.getOutputStream());
}
```
2. Using the HttpMessageConverter
2.1 配置HttpMessageConverter
`com.uk.bootintegrationall.springmvc.config.MediaTypeMessageConfig`
2.2 定义Controller
```java
    @GetMapping("/image2")
    @IgnoreAware
    public @ResponseBody byte[] getImageAsByteArray() throws IOException {
        InputStream in = servletContext.getResourceAsStream("/WEB-INF/images/image-example.jpg");
        return org.apache.commons.io.IOUtils.toByteArray(in);
    }
```
需要注意的是如果mvc对返回值进行了统一的封装, 就需要对该类型的返回值进行忽略, 否则会报错, 这里我直接在
`com.uk.bootintegrationall.springmvc.config.FastMvcResponseBodyAwareAdvice#getIsSupport` 里面直接返回类false
3. Using the ResponseEntity Class
```java
@RequestMapping(value = "/image3", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageAsResponseEntity() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        InputStream in = servletContext.getResourceAsStream("/WEB-INF/images/image-example.jpg");
        byte[] media = IOUtils.toByteArray(in);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        return responseEntity;
    }
```
需要注意的是如果mvc对返回值进行了统一的封装, 就需要对该类型的返回值进行忽略, 否则会报错, 这里我直接在
`com.uk.bootintegrationall.springmvc.config.FastMvcResponseBodyAwareAdvice#getIsSupport` 里面直接返回类false
4. Returning Image Using the Resource Class
暂时无法实现, 会报错.

### 3.12 分页查询
使用spring-data-jpa提供的分页组件, Pageable
配置: MediaTypeMessageConfig#addArgumentResolvers
使用: 
```java
    @GetMapping("/page")
    public void getCarPage(@PageableDefault Pageable pageable) {
        Pageable of = PageRequest.of(1, 10);
        System.out.println("pageable = " + pageable);
    }
```
请求
`GET http://localhost:8080/api/car/page?page=1&size=10&sort=name,desc&sort=age,asc`
pageable打印: `pageable = Page request [number: 1, size 10, sort: name: DESC,age: ASC]`
### 3.13 使用枚举作为Controller入参
正常情况下可以直接用枚举值作为参数使用, 但是如果请求传递的入参是个不合法入参, 就会报错, 为了避免这种情况, 可以使用自定义转换器来实现枚举值的转换或报错.
1. 定义转换器
`com.uk.bootintegrationall.springmvc.config.StringToEnumConverter`
2. 配置转换器
`com.uk.bootintegrationall.springmvc.config.MediaTypeMessageConfig#addFormatters`