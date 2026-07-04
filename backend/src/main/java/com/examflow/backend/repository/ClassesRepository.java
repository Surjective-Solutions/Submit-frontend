package com.examflow.backend.repository;

import com.examflow.backend.entity.Classes;
import com.examflow.backend.entity.Tutor;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassesRepository extends JpaRepository<Classes, Integer> {


    List<Classes> findByStatusAndTutor(Integer status, Tutor tutor);

    Classes findByClassSeqAndStatus(Integer classSeq, Integer status);
}
