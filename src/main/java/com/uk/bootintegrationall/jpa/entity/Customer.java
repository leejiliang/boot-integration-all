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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
