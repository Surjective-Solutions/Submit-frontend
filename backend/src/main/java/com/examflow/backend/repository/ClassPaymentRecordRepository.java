package com.examflow.backend.repository;

import java.time.Month;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.examflow.backend.entity.ClassPaymentRecord;
import com.examflow.backend.entity.Classes;

@Service
public interface ClassPaymentRecordRepository extends JpaRepository<ClassPaymentRecord, Integer> {

    List<ClassPaymentRecord> findByClassesAndStatus(Classes classes, Integer statusSeq);

    List<ClassPaymentRecord> findByClasses(Classes classes);

    ClassPaymentRecord findByClassesAndStatusAndMonth(Classes classes, Integer statusSeq, Integer month);

    ClassPaymentRecord findByClassesAndMonthAndYear(Classes classes, Integer month, Integer year);

    ClassPaymentRecord findByClassesAndMonthAndYearAndStatus(Classes classes, Integer month, Integer year,
            Integer status);

}
