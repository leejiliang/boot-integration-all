package com.uk.bootintegrationall.jpa.entity;

import com.uk.bootintegrationall.jpa.entity.listener.CustomerListener;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @Description TODO
 */
@Entity
@Getter
@Setter
@EntityListeners({AuditingEntityListener.class, CustomerListener.class})
public class Customer {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String address;

    @CreatedBy
    private Long creatorId;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedBy
    private Long modifierId;

    @LastModifiedDate
    private LocalDateTime modifyTime;

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

    @PrePersist
    void prePersist() {
        System.out.println("prePersist in entity");
    }

    @PostPersist
    void postPersist() {
        System.out.println("postPersist in entity");
    }

    @PreUpdate
    void preUpdate() {
        System.out.println("preUpdate in entity");
    }

    @PostUpdate
    void postUpdate() {
        System.out.println("postUpdate in entity");
    }

    @PreRemove
    void preRemove() {
        System.out.println("preRemove in entity");
    }

    @PostRemove
    void postRemove() {
        System.out.println("postRemove in entity");
    }

    @PostLoad
    void postLoad() {
        System.out.println("postLoad in entity");
    }
}
