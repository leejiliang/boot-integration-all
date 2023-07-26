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