package com.examflow.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examflow.backend.dto.ClassRequest;
import com.examflow.backend.dto.ClassResponse;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.StudentResponse;
import com.examflow.backend.service.StudentControllerManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import com.examflow.backend.dto.UserSignUpRequest;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    private final StudentControllerManager studentControllerManager;

    @Autowired
    public StudentController(StudentControllerManager studentControllerManager) {
        this.studentControllerManager = studentControllerManager;
    }

    @GetMapping("/get-all-students")
    public List<StudentResponse> getStudents() {
        return studentControllerManager.getAllStudents();
    }

    @GetMapping("/get-student/{id}")
    public StudentResponse getStudentById(@PathVariable Integer id){
        return studentControllerManager.getStudentById(id);
    }

    @PostMapping("/add-class-student")
    public GeneralResponse addClasstoStudent(@RequestBody ClassRequest classRequest) {
        System.out.println("reached to controller" + classRequest.getClassSeq());
        GeneralResponse response = studentControllerManager.addClassToStudent(classRequest.getClassSeq());
        return response;
    }

    @GetMapping("/get-all-enrolled-class")
    public List<ClassResponse> getAllEnrolledClass() {
        return studentControllerManager.getAllEnrolledClass();
    }

    @PutMapping("/update/{id}")
    public GeneralResponse updateStudent(@PathVariable Integer id, @RequestBody UserSignUpRequest studentRequest) {
        return studentControllerManager.updateStudent(id, studentRequest);
    }
}
