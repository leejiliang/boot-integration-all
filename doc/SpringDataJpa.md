# SpringDataJpa使用
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
## 1. 创建实体类
```java
@Entity
public class Customer{}
```
## 2. 创建Repository接口
```java
//public interface CustomerRepository extends Repository {
//public interface CustomerRepository extends CrudRepository<Customer, Long> {
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
}
```
通过继承不同的接口, 可以使得Repository拥有不同的功能. 具体选择可以根据实际场景来选择.
## 3. 选择性暴露部分接口
```java
@NoRepositoryBean
public interface MyRepository<T, ID> extends Repository<T, ID> {
    Optional<T> findById(ID id);
    <S extends T> S save(S entity);
}
```
## 4. 排序和分页
通过参数: Pageable pageable  和  Sort来实现

## 5. 返回结果的支持,如何返回DTO, 异步支持
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