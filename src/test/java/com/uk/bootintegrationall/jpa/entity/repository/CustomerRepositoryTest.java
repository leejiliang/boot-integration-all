package com.uk.bootintegrationall.jpa.entity.repository;

import com.uk.bootintegrationall.jpa.entity.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("测试findByName")
    void findByName() {
        var customerOnlyName = customerRepository.findByName("zhangsan");
        System.out.println(customerOnlyName.getName());
    }

    @Test
    void findByQueryName() {
        var customer = customerRepository.findByQueryName("zhangsan");
        System.out.println(customer.toString());
    }

    @Test
    @DisplayName("测试createCustomer")
    @Rollback(value = false)
    void createCustomer() {
        var customer = new Customer();
        customer.setEmail("1111@google.com");
        customer.setName("zhangsan");
        customer.setAddress("shanghai");
        customerRepository.save(customer);
    }
}