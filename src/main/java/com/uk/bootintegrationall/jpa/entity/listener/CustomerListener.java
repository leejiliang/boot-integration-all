package com.uk.bootintegrationall.jpa.entity.listener;

import com.uk.bootintegrationall.jpa.entity.Customer;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * @Description TODO
 */
@Slf4j
public class CustomerListener {

    @PrePersist
    void prePersist(Customer customer) {
        System.out.println("prePersist in CustomerListener");
    }

    @PostPersist
    void postPersist(Customer customer) {
        System.out.println("postPersist in CustomerListener");
    }

    @PreUpdate
    void preUpdate(Customer customer) {
        System.out.println("preUpdate in CustomerListener");
    }

    @PostUpdate
    void postUpdate(Customer customer) {
        System.out.println("postUpdate in CustomerListener");
    }

    @PreRemove
    void preRemove(Customer customer) {
        System.out.println("preRemove in CustomerListener");
    }

    @PostRemove
    void postRemove(Customer customer) {
        System.out.println("postRemove in CustomerListener");
    }

    @PostLoad
    void postLoad(Customer customer) {
        System.out.println("postLoad in CustomerListener");
    }
}
