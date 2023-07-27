package com.uk.bootintegrationall.jpa.entity.repository;

import com.uk.bootintegrationall.jpa.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description TODO
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
