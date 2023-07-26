package com.uk.bootintegrationall.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Description TODO
 */
@Entity
public class Customer {

    @Id
    private Long id;

    private String name;

    private String email;

    private String address;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", address='" + address + '\'' +
            '}';
    }
}
