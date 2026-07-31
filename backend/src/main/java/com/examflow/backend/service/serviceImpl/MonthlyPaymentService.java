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
import com.examflow.backend.entity.Student;
import com.examflow.backend.entity.StudentClass;
import com.examflow.backend.entity.StudentClassPaymentRecord;
import com.examflow.backend.enums.paymentStatus;
import com.examflow.backend.repository.ClassPaymentRecordRepository;
import com.examflow.backend.repository.ClassesRepository;
import com.examflow.backend.repository.StudentClassPaymentRecordsRepository;
import com.examflow.backend.repository.StudentClassesRepository;
import com.examflow.backend.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class MonthlyPaymentService {

    private final ClassPaymentRecordRepository classPaymentRecordRepository;
    private final StudentClassesRepository studentClassesRepository;
    private final StudentClassPaymentRecordsRepository studentClassPaymentRecordsRepository;
    private final ClassesRepository classesRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public MonthlyPaymentService(ClassPaymentRecordRepository classPaymentRecordRepository,
            StudentClassesRepository studentClassesRepository,
            StudentRepository studentRepository,
            StudentClassPaymentRecordsRepository studentClassPaymentRecordsRepository,
            ClassesRepository classesRepository) {
        this.classPaymentRecordRepository = classPaymentRecordRepository;
        this.studentClassesRepository = studentClassesRepository;
        this.studentClassPaymentRecordsRepository = studentClassPaymentRecordsRepository;
        this.studentRepository = studentRepository;
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
    public ClassPaymentRecord getOrCreateClassPaymentRecord(Classes classes, Integer month, Integer year,
            String username) {

        ClassPaymentRecord classPaymentRecord = classPaymentRecordRepository
                .findByClassesAndStatusAndMonth(classes, 2, month);
        if (classPaymentRecord != null) {
            return classPaymentRecord;
        }

        ClassPaymentRecord newPaymentRecord = new ClassPaymentRecord();
        newPaymentRecord.setClasses(classes);
        newPaymentRecord.setMonth(month);
        newPaymentRecord.setYear(year);
        newPaymentRecord.setStatus(2);
        newPaymentRecord.setCreatedBy(username);
        newPaymentRecord.setLastModifiedBy(username);
        newPaymentRecord.setCreatedDateTime(LocalDateTime.now());
        newPaymentRecord.setLastModifiedDateTime(LocalDateTime.now());
        newPaymentRecord.setClassPaymentRecordSearial(month + "-" + year + "-payment");

        return classPaymentRecordRepository.save(newPaymentRecord);
    }

    @Transactional
    public void generateStudentClassPaymentRecord() {
        System.out.println(
                "-------------------------------------------Generating Student payment Reacords---------------------------------------");
        List<Student> students = studentRepository.findByStatus(2);
        for (Student oneStudent : students) {
            System.out.println(
                    "-------------------------------------------Getting Students for payment Reacords---------------------------------------");

            List<StudentClass> enrolledClass = studentClassesRepository.findByStudentAndStatusSeq(oneStudent, 2);
            for (StudentClass studentClass : enrolledClass) {

                System.out.println(
                        "-------------------------------------------Getting Enrolled Class for Students---------------------------------------");

                List<ClassPaymentRecord> classPaymentRecords = classPaymentRecordRepository
                        .findByClasses(studentClass.getClasses());
                for (ClassPaymentRecord classPaymentRecord : classPaymentRecords) {
                    System.out.println(
                            "-------------------------------------------checking  respective payment records for Student---------------------------------------");
                    YearMonth enrolledDate = YearMonth.from(studentClass.getCreatedDateTime());
                    // enrolledDate = enrolledDate.minusMonths(1);

                    YearMonth classDate = YearMonth.of(classPaymentRecord.getYear(), classPaymentRecord.getMonth());
                    if (!classDate.isBefore(enrolledDate)) {
                        List<StudentClassPaymentRecord> existsStudentClassPaymentRecord = studentClassPaymentRecordsRepository
                                .findByStudentAndClassPaymentRecord(oneStudent, classPaymentRecord);
                        if (existsStudentClassPaymentRecord.size() == 0) {

                            StudentClassPaymentRecord studentClassPaymentRecord = new StudentClassPaymentRecord();

                            studentClassPaymentRecord.setClassPaymentRecord(classPaymentRecord);
                            studentClassPaymentRecord.setStudent(oneStudent);
                            studentClassPaymentRecord.setIsPayed(false);
                            studentClassPaymentRecord.setStatus(2);
                            studentClassPaymentRecord.setCreatedBy("SYSTEM");
                            studentClassPaymentRecord.setLastModifiedBy("SYSTEM");
                            studentClassPaymentRecord.setCreatedDateTime(LocalDateTime.now());
                            studentClassPaymentRecord.setLastModifiedDateTime(LocalDateTime.now());

                            studentClassPaymentRecordsRepository.save(studentClassPaymentRecord);
                            System.out.println(
                                    "-------------------------------------------Generated  respective payment records for Student---------------------------------------");

                        }
                    }
                }
            }
        }

        return;
    }

}
