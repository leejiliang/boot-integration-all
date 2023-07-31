package com.uk.bootintegrationall.jpa.entity.repository;

import com.uk.bootintegrationall.jpa.entity.Contact;
import com.uk.bootintegrationall.jpa.entity.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("mysql")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ContactRepository contactRepository;

    @Test
    @DisplayName("测试findByName")
    @Transactional
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
        customer.setName("zhangsan3");
        customer.setAddress("shanghai");

        var contact = new Contact();
        contact.setCustomer(customer);
        contact.setName("contact1");
        contact.setTel("123456789");

        var contact2 = new Contact();
        contact2.setCustomer(customer);
        contact2.setName("contact2");
        contact2.setTel("123456788");

        var contact3 = new Contact();
        contact3.setCustomer(customer);
        contact3.setName("contact3");
        contact3.setTel("123456788");

        customer.getContacts().add(contact);
        customer.getContacts().add(contact2);
        customer.getContacts().add(contact3);

        customerRepository.save(customer);
    }

    @Test
    @DisplayName("测试createCustomerUpdate")
    @Rollback(value = false)
    @Transactional
    void updateCustomer() {
        var customer = customerRepository.findById(1L);
        customer.ifPresent(i->i.setAddress(i.getAddress()+"ok"));
    }

    @Test
    @DisplayName("测试N+1问题")
    @Transactional
    void testOneToManyNPlusOne() {
        var customer = customerRepository.findAll();
    }

    @Test
    @DisplayName("测试N+1问题")
    @Transactional
    void testManyToOneNPlusOne() {
        var customer = contactRepository.findAll();
    }

    @Test
    @DisplayName("测试N+1问题")
    @Transactional
    void testFetchModelJoinNPlusOne() {
        var customer = customerRepository.findById(67L);
    }

}