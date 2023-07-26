package com.uk.bootintegrationall.jpa.entity;

import com.uk.bootintegrationall.jpa.entity.Book;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @Description TODO
 */
@Entity
@DiscriminatorValue("small_book")
public class SmallBook extends Book {
    private String typeName;
}
