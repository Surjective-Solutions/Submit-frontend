package com.examflow.backend.repository;

import com.examflow.backend.entity.Instructor;
import com.examflow.backend.entity.Tutor;
import com.examflow.backend.entity.TutorInstructor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorInstructorRepository extends JpaRepository<TutorInstructor, Integer> {

    List<TutorInstructor> findByTutorAndInstructorAndIsEngaged(Tutor tutor, Instructor instructor, Boolean isEngaged);

    List<TutorInstructor> findByTutorAndIsEngaged(Tutor tutor, Boolean isEngaged);
}
