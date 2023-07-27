package com.uk.bootintegrationall.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description TODO
 */

@Entity
@Getter
@Setter
@ToString(exclude = "students")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String address;

    private String course;

    @ManyToMany
    private Set<Student> students = new HashSet<>();
}
