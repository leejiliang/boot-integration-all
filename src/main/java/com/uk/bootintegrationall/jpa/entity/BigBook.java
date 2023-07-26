package com.uk.bootintegrationall.jpa.entity;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @Description TODO
 */
@Entity
@DiscriminatorValue("big_book")
public class BigBook extends Book {
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return super.toString()+ "BigBook{" +
            "typeName='" + typeName + '\'' +
            '}';
    }
}
