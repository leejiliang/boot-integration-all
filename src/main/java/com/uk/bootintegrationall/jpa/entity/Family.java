package com.uk.bootintegrationall.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @Description TODO
 */

@Entity
@Getter
@Setter
@ToString(exclude = "student")
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fatherName;

    private String motherName;

    @OneToOne
    private Student student;

}
