package com.uk.bootintegrationall.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

/**
 * @Description TODO
 */
@Entity
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String tel;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @BatchSize(size = 10)// 无效
    private Customer customer;
}
