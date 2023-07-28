# SpringDataJpa使用
参考文档: https://blog.csdn.net/fegus/article/details/124877919

# 基础配置
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
```
# 关键接口
`org.springframework.data.repository.Repository`
`org.springframework.data.repository.CrudRepository`
`org.springframework.data.repository.PagingAndSortingRepository`

这些接口分别定义了不同的方法, 用于实现不同的功能. 而且从上倒下依次呈现继承关系, 即子接口拥有父接口的所有方法.
- 实现类:
`org.springframework.data.jpa.repository.support.SimpleJpaRepository`

# 基本使用
1. 创建实体类
```java
@Entity
public class Customer{}
```
 2. 创建Repository接口
```java
//public interface CustomerRepository extends Repository {
//public interface CustomerRepository extends CrudRepository<Customer, Long> {
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
}
```
通过继承不同的接口, 可以使得Repository拥有不同的功能. 具体选择可以根据实际场景来选择.
 3. 选择性暴露部分接口
```java
@NoRepositoryBean
public interface MyRepository<T, ID> extends Repository<T, ID> {
    Optional<T> findById(ID id);
    <S extends T> S save(S entity);
}
```
 4. 排序和分页
通过参数: Pageable pageable  和  Sort来实现

 5. 返回结果的支持,如何返回DTO, 异步支持
- 异步
查询方法返回值类型可以是`Future<T>`或者`CompletableFuture<T>`, 也可以是`ListenableFuture<T>`, 但是需要添加`@EnableAsync`注解.
- 返回的类型支持
可以返回`List<T>`, `Iterable<T>`, `Page<T>`, `Slice<T>`, `Stream<T>`, `Future<T>`, `Optional<T>`, `T`等类型.
- 返回DTO
  - 定义多个entity. 不推荐
  - 直接返回DTO对象, 无法自定义构造函数, 否则会选择构造方法报错
  - 直接返回接口.

定义接口, 提供get方法
```java
public interface CustomerOnlyName {
  String getName();
}
```
定义查询
```java
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    CustomerOnlyName findByName(String name);
}
```
测试
```java
@Test
    @DisplayName("测试findByName")
    void findByName() {
        var customerOnlyName = customerRepository.findByName("zhangsan");
        System.out.println(customerOnlyName.getName());
    }
```
打印sql
```sql
Hibernate: select customer0_.name as col_0_0_ from customer customer0_ where customer0_.name=?
```
可以看到这里只查询了name属性.

## @Query

- 定义基本查询方法
JPQL语法
  https://docs.oracle.com/html/E13946_04/ejb3_langref.html
```java
//    @Query("from Customer where name = ?1")
@Query("from Customer where name = :name")
    Customer findByQueryName(String name);
```
以上两种传参方式均支持.
- 动态查询
## @Entity
必须有主键或复合主键
所有字段都会被映射到数据库, 除非使用`@Transient`注解
- @Id表示主键, GeneratedValue表示主键生成策略
- @Basic 表示普通字段, 默认
- @Transient 表示不映射到数据库
- @Temporal 表示日期类型, 可以指定精度
- @Enumerated 表示枚举类型, 默认是枚举的序号, 可以指定为枚举的名称
- @IdClass 表示复合主键
- @Embedded 表示嵌入式对象
- @EmbeddedId 表示嵌入式主键
## 实体之间的继承关系
- @Inheritance(strategy = InheritanceType.SINGLE_TABLE) 表示多个子类和父类在同一张表中
1. 定义父类
```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "book_type", discriminatorType = DiscriminatorType.STRING)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String authorName;

```
2. 定义子类1
```java
@Entity
@DiscriminatorValue("big_book")
public class BigBook extends Book {
    private String typeName;
}
```
3. 定义子类2
```java
@Entity
@DiscriminatorValue("small_book")
public class SmallBook extends Book {
    private String typeName;
}
```
4. 定义子类1Repository
```java
@Repository
public interface BigBookRepository extends JpaRepository<BigBook, Long> {

    List<BigBook> findBigBookByAuthorName(String authorName);
}
```
5. 测试
```java
    @Test
    void findBigBookByAuthorName() {

        var bigBook = new BigBook();
        bigBook.setAuthorName("zhangsan");
        bigBook.setTitle("红楼梦");
        bigBook.setTypeName("古典小说");
        bigBookRepository.saveAndFlush(bigBook);
        bigBookRepository.findBigBookByAuthorName("zhangsan").forEach(System.out::println);
    }
```
6. 打印sql
```sql
select bigbook0_.id as id2_0_, bigbook0_.author_name as author_n3_0_, bigbook0_.title as title4_0_, bigbook0_.type_name as type_nam5_0_ from book bigbook0_ where bigbook0_.book_type='big_book' and bigbook0_.author_name=?
```
可以看到这里自动加上了`book_type='big_book'`的条件
- //@Inheritance(strategy = InheritanceType.JOINED)
  //@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
这两种模式都是将父类和子类分开存储, 但是前者是通过外键关联, 后者是直接存储.

## 实体之间的关联关系
![relation](https://image-blog-lee.oss-cn-hangzhou.aliyuncs.com/uPic/2hbHCz.png)
所有关系聚合对象: 
`com.uk.bootintegrationall.jpa.entity.Student`
- 原则: 
1. 在被维护外键的Entity中定义mappedBy属性, 例如: `@OneToMany(mappedBy = "student")`
2. 在维护外键的Entity中定义外键属性, 例如: `@JoinColumn(name = "student_id")`
3. 在OneToMany中删除Many属性时, 需要在One属性中添加`orphanRemoval = true`属性, 否则不会执行delete.
4. 如果需要自定义外键属性名, 通过joinColumn 来指定.
5. 多对多的删除, 需要通过双方解除绑定关系来执行关系删除操作.

## Jackson
- jpa实体之间的关联关系, 需要注意死循环
ToString 死循环
Lombok注解: @ToString(exclude = "students")
ToJson 死循环
Jackson注解: @JsonIgnoreProperties("students")

# 高阶用法
## JpaSpecificationExecutor
### QueryByExampleExecutor
JpaRepository 继承 JpaSpecificationExecutor 例如: 
`com.uk.bootintegrationall.jpa.entity.repository.StudentRepository`
创建查询: 
```java
@Test
@DisplayName("Specification测试")
@Rollback(value = false)
@Transactional
void testSpecQuery() {
    var studentSpecification = new Specification<Student>() {
        @Override
        public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            var nameLike = criteriaBuilder.like(root.get("name"), "%三%");
            var emailLike = criteriaBuilder.like(root.get("email"), "%@email.com");
            var subItemIn = criteriaBuilder.in(root.join("teachers").get("course")).value(List.of("语文","英语"));
            var nameOrMail = criteriaBuilder.or(nameLike, emailLike);
            var finalCriteria = criteriaBuilder.and(nameOrMail, subItemIn);
            return query.where(finalCriteria).getRestriction();
        }
    };
    studentRepository.findAll(studentSpecification).forEach(System.out::println);
}
```
打印sql
```sql
select student0_.id      as id1_4_,
       student0_.address as address2_4_,
       student0_.email   as email3_4_,
       student0_.name    as name4_4_
from student student0_
         inner join teacher_students teachers1_ on student0_.id = teachers1_.students_id
         inner join teacher teacher2_ on teachers1_.teachers_id = teacher2_.id
where (student0_.name like ? or student0_.email like ?)
  and (teacher2_.course in (?, ?));
```
## 自定义Repository
### EntityManager
注入
@PersistenceContext
EntityManager
或者通过构造函数注入
1. 定义接口
`com.uk.bootintegrationall.jpa.entity.repository.CustomStudentRepository`
2. 实现接口
`com.uk.bootintegrationall.jpa.entity.repository.CustomStudentRepositoryImpl`
3. 接口继承
`com.uk.bootintegrationall.jpa.entity.repository.StudentRepository`
4. 测试
```java
    @Test
    @Transactional
    @Rollback(value = false)
    void testImplRepository(){
        var byId = studentRepository.findById(44L);
        byId.ifPresent(i->{
            studentRepository.deleteByEmail(i);
        });
    }
```
还可以通过重写SimpleJpaRepository来实现类来修改Repository的功能. 例如: 将所有的删除操作修改为逻辑删除.
在启动类中注入重写后的基础类即可.
@EnableJparepositories(repositoryBaseClass = CustomSimpleJpaRepository.class)
## Jpa审计功能Auditing
有三种实现审计功能的方式:
- 方式一: 
1. 实体类中添加注解属性, 类上添加注解: @EntityListeners(AuditingEntityListener.class)

```java
    @CreatedBy
    private Long creatorId;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedBy
    private Long modifierId;

    @LastModifiedDate
    private LocalDateTime modifyTime;
```
2. 实现AuditorAware接口
```java
@Component
public class MyAuditorAware implements AuditorAware<Long> {
    /**
     * 可以从Security, req中获取当前用户信息
     * @return
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(888L);
    }
}
```
3. 开启审计功能
`@EnableJpaAuditing`
- 方式二
第一步方式替换为: 实现类实现Auditable接口, 对实体入侵比较严重, 不推荐.
- 方式三
@MappedSuperClass
1. 定义基类
```java
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedBy
    private Long creatorId;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedBy
    private Long modifierId;

    @LastModifiedDate
    private LocalDateTime modifyTime;
}
```
2. 实体类继承基类即可.
## @Entity里面的回调方法
支持的回调方法: 
- @PrePersist
- @PostPersist
- @PreUpdate
- @PostUpdate
- @PreRemove
- @PostRemove
- @PostLoad
这些回调方法可以放在基类中, 也可以放在实体类中, 或者放在一个自定义的类中, 通过注解@EntityListeners 引入.
使用示例: 
`com.uk.bootintegrationall.jpa.entity.Customer`
`com.uk.bootintegrationall.jpa.entity.listener.CustomerListener`
通过日志可以看出, 回调方法的执行顺序为: 自定义 > 基类 > 实体类
需要注意的是, 如果在回调方法中发生了异常, 会导致事务回滚.
回调方法中, 入参可以是实体类, 也可以是基类, 空, 或者Object.

如果查询的时候, 不需要回调方法, 可以通过注解: @ExcludeDefaultListeners 来排除回调方法. @ExcludeSuperclassListeners 可以排除基类的回调方法.

如果查询没有使用EntityManager, 可能不会触发回调方法的调用, 例如: 
`com.uk.bootintegrationall.jpa.entity.repository.CustomerRepository.findByName` 返回值不是Entity.

## 乐观锁机制和重试机制
在实体类中添加注解: @Version, 或者添加到BaseEntity中
测试并发锁: 
```java
@Test
@Transactional
@Rollback(value = false)
@DisplayName("乐观锁测试")
void testParallelUpdate() {
    var student = studentRepository.findById(44L);
    var threadPool = Executors.newFixedThreadPool(3);
    student.ifPresent(i -> {
        i.setName(i.getName() + "Q");
        IntStream.rangeClosed(1, 3).forEach(j -> {
            threadPool.submit(() -> {
                studentRepository.save(i);
            });
        });
    });
    threadPool.shutdown();
}
```
触发乐观锁异常: 
```java
org.springframework.orm.ObjectOptimisticLockingFailureException: Batch update returned unexpected row count from update [0]; actual row count: 0; expected: 1; statement executed: update student set create_time=?, creator_id=?, modifier_id=?, modify_time=?, version=?, address=?, email=?, is_deleted=?, name=? where id=? and version=?; nested exception is org.hibernate.StaleStateException: Batch update returned unexpected row count from update [0]; actual row count: 0; expected: 1; statement executed: update student set create_time=?, creator_id=?, modifier_id=?, modify_time=?, version=?, address=?, email=?, is_deleted=?, name=? where id=? and version=?
```
解决方案:
1. 重试机制
引入Spring重试组件:
```xml
<dependency>
  <groupId>org.springframework.retry</groupId>
  <artifactId>spring-retry</artifactId>
</dependency>
```
2. 开启重试
`@EnableRetry`
3. 在指定的方法上添加注解: @Retryable

JPA的悲观锁机制:
1. 在方法上添加注解: @Lock(LockModeType.PESSIMISTIC_WRITE)
```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
Optional<Student> findByEmail(String email);
```

## 数据源
参开文章: https://bgmbk.blog.csdn.net/article/details/124895223
Java定义的JDBC协议标准中有一个数据源接口: javax.sql.DataSource, 该接口定义了数据库连接池的一些基本操作, 例如: 获取连接, 关闭连接, 获取元数据等.
常见的第三方实现有: C3P0, DBCP, Druid, HikariCP等. 目前比较常用的有: Druid, HikariCP.其中 HikariCP是SpringBoot2.0默认的数据源.
`com.zaxxer.hikari.HikariDataSource`
其核心逻辑就是方法: getConnection(), 该方法会从连接池中获取一个连接, 如果没有连接池, 就创建一个连接池, 如果连接池中没有连接, 则会创建一个连接, 并放入连接池中.
- 数据源的关键配置
`com.zaxxer.hikari.HikariConfig`
- 如何获取连接
- 加载原理和过程
自动配置类: 
```java
@AutoConfiguration(
  before = {SqlInitializationAutoConfiguration.class}
  )
  @ConditionalOnClass({DataSource.class, EmbeddedDatabaseType.class})
  @ConditionalOnMissingBean(
  type = {"io.r2dbc.spi.ConnectionFactory"}
  )
  @EnableConfigurationProperties({DataSourceProperties.class})
  @Import({DataSourcePoolMetadataProvidersConfiguration.class})
  public class DataSourceAutoConfiguration {
```
核心方法: 
```java
    @Configuration(
        proxyBeanMethods = false
    )
    @Conditional({PooledDataSourceCondition.class})
    @ConditionalOnMissingBean({DataSource.class, XADataSource.class})
    @Import({DataSourceConfiguration.Hikari.class, DataSourceConfiguration.Tomcat.class, DataSourceConfiguration.Dbcp2.class, DataSourceConfiguration.OracleUcp.class, DataSourceConfiguration.Generic.class, DataSourceJmxConfiguration.class})
    protected static class PooledDataSourceConfiguration {
        protected PooledDataSourceConfiguration() {
        }
    }
```
这里Import了所有的springBoot支持的数据源, 具体哪个生效, 在DataSourceConfiguration中通过@Conditional注解来判断.
以Hikari为例: 
```java
    @Configuration(
        proxyBeanMethods = false
    )
    @ConditionalOnClass({HikariDataSource.class})// spring 默认引入
    @ConditionalOnMissingBean({DataSource.class})// 一开始没有数据源
    @ConditionalOnProperty(// 默认配置
        name = {"spring.datasource.type"},
        havingValue = "com.zaxxer.hikari.HikariDataSource",
        matchIfMissing = true
    )
    static class Hikari {
        Hikari() {
        }

        @Bean
        @ConfigurationProperties(
            prefix = "spring.datasource.hikari"// 所有符合该前缀的配置都会被加载到DataSourceProperties中
        )
        HikariDataSource dataSource(DataSourceProperties properties) {
            HikariDataSource dataSource = (HikariDataSource)DataSourceConfiguration.createDataSource(properties, HikariDataSource.class);
            if (StringUtils.hasText(properties.getName())) {
                dataSource.setPoolName(properties.getName());
            }

            return dataSource;
        }
    }
```
通过condition可以看出SpringBoot对Hikari的支持是默认的, 如果没有配置spring.datasource.type, 则默认使用HikariDataSource.
常用配置: 
```properties
##数据源的配置：logger=Slf4JLogger&profileSQL=true是用来debug显示sql的执行日志的
spring.datasource.url=jdbc:mysql://localhost:3306/test?logger=Slf4JLogger&profileSQL=true
spring.datasource.username=root
spring.datasource.password=E6kroWaR9F
##采用默认的
#spring.datasource.hikari.connectionTimeout=30000
#spring.datasource.hikari.idleTimeout=300000
##指定一个链接池的名字，方便我们分析线程问题
spring.datasource.hikari.pool-name=jpa-hikari-pool
##最长生命周期15分钟够了
spring.datasource.hikari.maxLifetime=900000
spring.datasource.hikari.maximumPoolSize=8
##最大和最小相对应减少创建线程池的消耗；
spring.datasource.hikari.minimumIdle=8
spring.datasource.hikari.connectionTestQuery=select 1 from dual
##当释放连接到连接池之后，采用默认的自动提交事务
spring.datasource.hikari.autoCommit=true
##用来显示链接测trace日志
logging.level.com.zaxxer.hikari.HikariConfig=DEBUG
logging.level.com.zaxxer.hikari=TRACE
logging.level.com.zaxxer.hikari.pool.HikariPool=DEBUG
```
## Entity和数据库字段名的映射策略
