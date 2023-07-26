package com.uk.bootintegrationall.jpa.entity.repository;

import com.uk.bootintegrationall.jpa.entity.BigBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BigBookRepositoryTest {

    @Autowired
    private BigBookRepository bigBookRepository;
    @Test
    void findBigBookByAuthorName() {

        var bigBook = new BigBook();
        bigBook.setAuthorName("zhangsan");
        bigBook.setTitle("红楼梦");
        bigBook.setTypeName("古典小说");
        bigBookRepository.saveAndFlush(bigBook);
        bigBookRepository.findBigBookByAuthorName("zhangsan").forEach(System.out::println);
    }
}