package com.uk.bootintegrationall.jpa.entity.repository;

import com.uk.bootintegrationall.jpa.entity.Student;

/**
 * @Description TODO
 */
public interface CustomStudentRepository {

    void deleteByEmail(Student email);
}
