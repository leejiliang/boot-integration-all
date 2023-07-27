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
public class QQAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Long id;

    private String qqNumber;

    private String qqPassword;

    private String qqName;

    @ManyToOne
    private Student student;
}
