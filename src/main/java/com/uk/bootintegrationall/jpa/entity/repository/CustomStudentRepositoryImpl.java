package com.uk.bootintegrationall.jpa.entity.repository;

import com.uk.bootintegrationall.jpa.entity.Student;

import javax.persistence.EntityManager;

/**
 * @Description TODO
 */
public class CustomStudentRepositoryImpl implements CustomStudentRepository{
    private final EntityManager entityManager;
    public CustomStudentRepositoryImpl(EntityManager entityManager) {
        this.entityManager=entityManager;
    }
    @Override
    public void deleteByEmail(Student email) {
        email.setDeleted(true);
        entityManager.merge(email);
    }
}
