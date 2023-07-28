package com.uk.bootintegrationall.jpa.service;

import com.uk.bootintegrationall.jpa.entity.Student;
import com.uk.bootintegrationall.jpa.entity.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import javax.sql.DataSource;
import java.util.Optional;

/**
 * @Description TODO
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {
    private final StudentRepository studentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(value = {OptimisticLockException.class}, maxAttempts = 5)
    public void updateStudent(Long id) {
        var studentOptional = studentRepository.findById(id);
        studentOptional.ifPresent(i -> {
            try {
                var oldVersion = i.getVersion();
                Thread.sleep(500);
                i.setName(i.getName() + "P");
                var saved = studentRepository.save(i);
                log.info(Thread.currentThread().getName() + "更新成功" + "oldVersion:" + oldVersion + "newVersion:" + saved.getVersion());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
