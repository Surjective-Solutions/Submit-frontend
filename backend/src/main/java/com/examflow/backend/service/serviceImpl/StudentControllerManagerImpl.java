package com.examflow.backend.service.serviceImpl;

import com.examflow.backend.repository.StudentClassesRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.StudentResponse;
import com.examflow.backend.entity.Classes;
import com.examflow.backend.entity.Student;
import com.examflow.backend.entity.StudentClass;
import com.examflow.backend.repository.ClassesRepository;
import com.examflow.backend.repository.StudentRepository;
import com.examflow.backend.service.StudentControllerManager;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StudentControllerManagerImpl implements StudentControllerManager {

    private final StudentClassesRepository studentClassesRepository;
    private final StudentRepository studentRepository;
    private final ClassesRepository classesRepository;
    private HttpServletRequest request;

    @Autowired
    public StudentControllerManagerImpl(StudentRepository studentRepository,
            ClassesRepository classesRepository,
            HttpServletRequest request, StudentClassesRepository studentClassesRepository) {
        this.studentRepository = studentRepository;
        this.classesRepository = classesRepository;
        this.request = request;
        this.studentClassesRepository = studentClassesRepository;
    }

    @Override
    public List<StudentResponse> getAllStudents() {

        List<Student> studentList = studentRepository.findByStatus(2);

        List<StudentResponse> studentResponsesList = new ArrayList<>();

        for (Student student : studentList) {
            StudentResponse studentResponse = new StudentResponse();
            studentResponse.setId(student.getStudentSeq());
            studentResponse.setStudent_number(null);
            studentResponse.setFirst_name(student.getFirstName());
            studentResponse.setLast_name(student.getLastName());
            studentResponse.setDate_of_birth(student.getDob());
            studentResponse.setEmail(student.getEmail());
            studentResponse.setGender(student.getGender());
            studentResponse.setGrade(student.getGrade());
            studentResponse.setContact_number(student.getContactNumber());
            studentResponse.setWhatsapp_number(student.getWhatsappNumber());
            studentResponse.setSchool_name(student.getSchoolName());
            studentResponse.setSubject_stream(student.getSubjectStream());
            studentResponse.setSubject_stream(student.getSubjectStream());
            studentResponse.setGuardian_name(student.getGuardianName());
            studentResponse.setGuardian_contact(student.getGuardianContactNumber());
            studentResponse.setAddress(student.getAddress());
            studentResponse.setDistrict(student.getDistrict());
            studentResponse.setSubject_stream(student.getSubjectStream());
            if (student.getStatus() == 2) {
                studentResponse.setStatus("ACTIVE");

            } else {
                studentResponse.setStatus("INACTIVE");
            }
            studentResponsesList.add(studentResponse);
        }

        return studentResponsesList;
    }

    @Override
    public GeneralResponse addClassToStudent(Integer classId) {

        GeneralResponse generalResponse = new GeneralResponse();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Integer studentSeq = (Integer) request.getAttribute("userId");

        Student student = studentRepository.findByStudentSeq(studentSeq);
        if (student == null) {
            generalResponse.setIsSuccess(false);
            generalResponse.setMessage("student not found");
            return generalResponse;
        } else {
            StudentClass studentClass = new StudentClass();
            Classes classes = classesRepository.findByClassSeqAndStatus(classId, 2);

            studentClass.setClasses(classes);
            studentClass.setStudent(student);
            studentClass.setCreatedBy(username);
            studentClass.setLastModifiedBy(username);
            studentClass.setLastModifiedDateTime(LocalDateTime.now());
            studentClass.setCreatedDateTime(LocalDateTime.now());
            studentClass.setStatusSeq(2);
            studentClass.setMonthlyFee(classes.getMonthlyFee());

            studentClassesRepository.save(studentClass);

            generalResponse.setIsSuccess(true);
            generalResponse.setMessage("Class Addedd to your Class " + username);

            return generalResponse;
        }

    }

}
