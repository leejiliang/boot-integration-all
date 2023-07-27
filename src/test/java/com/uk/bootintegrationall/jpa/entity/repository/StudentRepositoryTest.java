package com.uk.bootintegrationall.jpa.entity.repository;

import com.uk.bootintegrationall.jpa.entity.Family;
import com.uk.bootintegrationall.jpa.entity.QQAccount;
import com.uk.bootintegrationall.jpa.entity.Student;
import com.uk.bootintegrationall.jpa.entity.Teacher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@DisplayName("学生仓库测试")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    @DisplayName("插入测试")
    @Transactional
    @Rollback(value = false)
    public void testInsert() {
        var student = new Student();
        student.setName("张三");
        student.setEmail("1111@test.com");
        student.setAddress("北京市");

        var teacher1 = new Teacher();
        teacher1.setName("李四");
        teacher1.setEmail("112@test.com");
        teacher1.setAddress("北京市");
        teacher1.setCourse("语文");

        var teacher2 = new Teacher();
        teacher2.setName("王五");
        teacher2.setEmail("113@test.com");
        teacher2.setAddress("北京市");
        teacher2.setCourse("数学");

        var qqAccount = new QQAccount();
        qqAccount.setQqName("张三QQ");
        qqAccount.setQqNumber("111111");
        qqAccount.setQqPassword("111111");

        var qqAccount2 = new QQAccount();
        qqAccount2.setQqName("张三QQ2");
        qqAccount2.setQqNumber("222222");
        qqAccount2.setQqPassword("11122222111");


        var family = new Family();
        family.setFatherName("张三爸爸");
        family.setMotherName("张三妈妈");

        student.getTeachers().add(teacher1);
        student.getTeachers().add(teacher2);
        student.getQqAccount().add(qqAccount);
        student.getQqAccount().add(qqAccount2);
        student.setFamily(family);

        teacher1.getStudents().add(student);
        teacher2.getStudents().add(student);
        qqAccount.setStudent(student);
        qqAccount2.setStudent(student);
        family.setStudent(student);
        studentRepository.save(student);

//        studentRepository.flush();
        var all = studentRepository.findAll();
        all.forEach(System.out::println);

    }

    @Test
    @DisplayName("删除测试")
    @Rollback(value = false)
    public void testDelete() {
        studentRepository.deleteById(32L);
    }

    @Test
    @DisplayName("删除OneToMany测试")
    @Rollback(value = false)
    @Transactional
    public void testDeleteOneToMany() {
        var student = studentRepository.findById(44L).orElseThrow();
//        var any = student.getQqAccount().stream()
//            .filter(i -> i.getQqNumber().equals("222222"))
//            .findAny();
//        any.ifPresent(i->{
//            i.setStudent(null);
//            student.getQqAccount().remove(i);
//        });
        student.getQqAccount().forEach(i->i.setStudent(null));
        student.getQqAccount().clear();
        studentRepository.save(student);
    }

    @Test
    @DisplayName("删除ManyToMany测试")
    @Rollback(value = false)
    @Transactional
    void testDeleteManyToMany() {
        var student = studentRepository.findById(44L).orElseThrow();
        var course = student.getTeachers().stream()
            .filter(i -> i.getCourse().equals("数学"))
            .findAny();
        course.ifPresent(i -> {
            i.getStudents().remove(student);
            student.getTeachers().remove(i);
        });
        studentRepository.save(student);
    }
}