package com.examflow.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.examflow.backend.entity.StudentClass;

@Service
public interface StudentClassesRepository extends JpaRepository<StudentClass, Integer> {

}
