package com.examflow.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.examflow.backend.dto.ClassRequest;
import com.examflow.backend.dto.ClassResponse;
import com.examflow.backend.dto.GeneralResponse;

@Service
public interface ClassControllerManager {

    GeneralResponse createClass(ClassRequest classRequest);
    
    List<ClassResponse> getAllClasses();
}
    