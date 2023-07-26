package com.uk.bootintegrationall.jpa.entity.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}