package com.examflow.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.examflow.backend.dto.AnswerSheetUploadRequest;
import com.examflow.backend.dto.ClassResponse;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.PaperUploadRequest;
import com.examflow.backend.dto.RegradeRequestCreateRequest;
import com.examflow.backend.dto.RegradeRequestResponse;
import com.examflow.backend.dto.UserSignUpRequest;
import com.examflow.backend.dto.StudentResponse;
import com.examflow.backend.dto.QuestionGradeResponse;
import com.examflow.backend.entity.Student;

@Service
public interface StudentControllerManager {

    List<StudentResponse> getAllStudents();

    GeneralResponse addClassToStudent(Integer classId);

    List<ClassResponse> getAllEnrolledClass();

    StudentResponse getStudentById(Integer id);

    GeneralResponse uploadAnswerSheet(AnswerSheetUploadRequest answerSheetUploadRequest, MultipartFile answerSheet);

    GeneralResponse updateStudent(Integer id, UserSignUpRequest studentRequest);

    GeneralResponse removeClassFromStudent(Integer classId);

    List<QuestionGradeResponse> getGradeDetailsForPaper(Integer paperId);

    GeneralResponse createRegradeRequest(Integer paperId, RegradeRequestCreateRequest regradeRequestCreateRequest);

    RegradeRequestResponse getRegradeRequestForPaper(Integer paperId);
}
