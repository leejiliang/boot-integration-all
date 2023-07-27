package com.uk.bootintegrationall.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@ToString(callSuper = false)
public class Student extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String address;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private Family family;

    private boolean isDeleted;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QQAccount> qqAccount = new HashSet<>();

    @ManyToMany(mappedBy = "students", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("students")
    private Set<Teacher> teachers = new HashSet<>();
}
