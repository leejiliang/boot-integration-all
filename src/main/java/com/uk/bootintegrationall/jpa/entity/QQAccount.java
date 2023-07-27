package com.uk.bootintegrationall.jpa.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @Description TODO
 */
@Entity
@Getter
@Setter
@ToString(exclude = "student")
@NoArgsConstructor
@AllArgsConstructor
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
