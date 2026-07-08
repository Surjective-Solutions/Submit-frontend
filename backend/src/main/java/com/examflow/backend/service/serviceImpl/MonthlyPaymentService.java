package com.examflow.backend.service.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examflow.backend.entity.ClassPaymentRecord;
import com.examflow.backend.entity.Classes;
import com.examflow.backend.repository.ClassPaymentRecordRepository;
import com.examflow.backend.repository.ClassesRepository;

import jakarta.transaction.Transactional;

@Service
public class MonthlyPaymentService {

    private final ClassPaymentRecordRepository classPaymentRecordRepository;
    private final ClassesRepository classesRepository;

    @Autowired
    public MonthlyPaymentService(ClassPaymentRecordRepository classPaymentRecordRepository,
            ClassesRepository classesRepository) {
        this.classPaymentRecordRepository = classPaymentRecordRepository;
        this.classesRepository = classesRepository;
    }

    @Transactional
    public void generateClassPaymentRecord() {

        System.out.println("Genarating CalssPayment Records--------------------------------------------------");
        List<Classes> allClasses = classesRepository.findByStatus(2);
        for (Classes classes : allClasses) {
            YearMonth createdYearMonth = YearMonth.from(classes.getCreatedDateTime());
            YearMonth currentYearMonth = YearMonth.now();

            YearMonth month = createdYearMonth;
            while (!month.isAfter(currentYearMonth)) {
                Integer monthnumber = month.getMonthValue();
                Integer yearnumber = month.getYear();
                ClassPaymentRecord classPaymentRecord = classPaymentRecordRepository
                        .findByClassesAndMonthAndYear(classes, monthnumber, yearnumber);
                if (classPaymentRecord == null) {
                    ClassPaymentRecord newPaymentRecord = new ClassPaymentRecord();
                    newPaymentRecord.setClasses(classes);
                    newPaymentRecord.setMonth(monthnumber);
                    newPaymentRecord.setYear(yearnumber);
                    newPaymentRecord.setStatus(2);
                    newPaymentRecord.setCreatedBy("SYSTEM");
                    newPaymentRecord.setLastModifiedBy("SYSTEM");
                    newPaymentRecord.setCreatedDateTime(LocalDateTime.now());
                    newPaymentRecord.setLastModifiedDateTime(LocalDateTime.now());
                    newPaymentRecord.setClassPaymentRecordSearial(monthnumber + "-" + yearnumber + '-' + "payment");
                    classPaymentRecordRepository.save(newPaymentRecord);
                }

                System.out.println(month + "Class payment Record Genrated");
                month = month.plusMonths(1);
            }

        }
        return;
    }

    @Transactional
    public void generateStudentClassPaymentRecord() {
        return;
    }

}
