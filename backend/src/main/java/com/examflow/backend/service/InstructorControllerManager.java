package com.examflow.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.examflow.backend.dto.InstructorResponse;
import com.examflow.backend.entity.Instructor;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.InstructorSignUpRequest;
import com.examflow.backend.dto.InstructorTeacherResponse;
import com.examflow.backend.dto.RegradeRequestResponse;
import com.examflow.backend.dto.SubmitGradeResponse;

@Service
public interface InstructorControllerManager {

    List<InstructorResponse> getAllInstructors();
    InstructorResponse getInstructorById(Integer id);
    GeneralResponse updateInstructor(Integer id, InstructorSignUpRequest instructorRequest);

    List<InstructorTeacherResponse> getIntructorTeachers();

    GeneralResponse GradeSubmission(SubmitGradeResponse submitGradeResponse);

    GeneralResponse editSubmissionGrade(SubmitGradeResponse submitGradeResponse);

    List<RegradeRequestResponse> getPendingRegradeRequests();

    RegradeRequestResponse getRegradeRequestById(Integer regradeRequestSeq);
}
