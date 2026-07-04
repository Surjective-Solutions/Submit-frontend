package com.examflow.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examflow.backend.dto.ClassRequest;
import com.examflow.backend.dto.ClassResponse;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.PaperUploadRequest;
import com.examflow.backend.entity.UplaodPaper;
import com.examflow.backend.service.ClassControllerManager;

@RestController
@RequestMapping("/api/class")
@CrossOrigin(origins = "http://localhost:3000")
public class ClassController {

    private final ClassControllerManager classControllerManager;

    @Autowired
    public ClassController(ClassControllerManager classControllerManager) {
        this.classControllerManager = classControllerManager;
    }

    @PostMapping("/create")
    public GeneralResponse createClass(@RequestBody ClassRequest classRequest) {
        System.out.println("reached to controller");
        GeneralResponse response = new GeneralResponse();
        response = classControllerManager.createClass(classRequest);
        return response;
    }


    @GetMapping("/get-all-classes")
    public List<ClassResponse> getAllClasses() {
        return classControllerManager.getAllClasses();
    }

    @PostMapping("/{classId}/uploadpaper")
    public GeneralResponse uploadPapers(@RequestBody PaperUploadRequest paperUploadRequest,
            @PathVariable Integer classId) {
        System.out.println("reached to controller");
        GeneralResponse response = new GeneralResponse();
        response = classControllerManager.uploadPapers(paperUploadRequest, classId);
        return response;
    }
}
