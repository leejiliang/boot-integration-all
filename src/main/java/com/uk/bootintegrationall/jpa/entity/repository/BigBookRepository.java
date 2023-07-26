package com.uk.bootintegrationall.jpa.entity.repository;

import com.uk.bootintegrationall.jpa.entity.BigBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description TODO
 */
@Repository
public interface BigBookRepository extends JpaRepository<BigBook, Long> {

    List<BigBook> findBigBookByAuthorName(String authorName);
}
