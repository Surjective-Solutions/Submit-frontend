package com.examflow.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.examflow.backend.dto.CashierResponse;
import com.examflow.backend.dto.InstructorResponse;
import com.examflow.backend.service.InstructorControllerManager;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.InstructorSignUpRequest;

@RestController
@RequestMapping("/api/instructor")
@CrossOrigin(origins = "http://localhost:3000")
public class InstructorController {

    private final InstructorControllerManager instructorControllerManager;

    @Autowired
    public InstructorController(InstructorControllerManager instructorControllerManager) {

        this.instructorControllerManager = instructorControllerManager;
    }

    @GetMapping("/get-all-instructors")
    public List<InstructorResponse> getInstructors() {
        return instructorControllerManager.getAllInstructors();
    }
    @GetMapping("/get-instructor/{id}")
    public InstructorResponse getInstructorById(@PathVariable Integer id) {
        return instructorControllerManager.getInstructorById(id);
    }

    @PutMapping("/update/{id}")
    public GeneralResponse updateInstructor(@PathVariable Integer id, @RequestBody InstructorSignUpRequest instructorRequest) {
        return instructorControllerManager.updateInstructor(id, instructorRequest);
    }

}
