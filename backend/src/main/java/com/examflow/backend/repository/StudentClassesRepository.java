package com.examflow.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.examflow.backend.entity.Student;
import com.examflow.backend.entity.StudentClass;
import com.examflow.backend.entity.Classes;

@Service
public interface StudentClassesRepository extends JpaRepository<StudentClass, Integer> {

    List<StudentClass> findByStudentAndStatusSeq(Student student, Integer statusSeq);

    StudentClass findByStudentAndClassesAndStatusSeq(Student student, Classes classes, Integer statusSeq);


}
