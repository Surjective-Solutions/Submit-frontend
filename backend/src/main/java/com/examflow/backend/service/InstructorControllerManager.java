package com.examflow.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.examflow.backend.dto.InstructorResponse;
import com.examflow.backend.entity.Instructor;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.InstructorSignUpRequest;
import com.examflow.backend.dto.InstructorTeacherResponse;

@Service
public interface InstructorControllerManager {

    List<InstructorResponse> getAllInstructors();
    InstructorResponse getInstructorById(Integer id);
    GeneralResponse updateInstructor(Integer id, InstructorSignUpRequest instructorRequest);

    List<InstructorTeacherResponse> getIntructorTeachers();
}
