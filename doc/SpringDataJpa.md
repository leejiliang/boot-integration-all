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


