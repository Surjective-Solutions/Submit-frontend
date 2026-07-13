package com.examflow.backend.service.serviceImpl;

import com.examflow.backend.repository.StudentClassesRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import com.examflow.backend.dto.ClassResponse;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.MonthPapersResponse;
import com.examflow.backend.dto.PaperResponse;
import com.examflow.backend.dto.StudentClassPaymentRecordResponse;
import com.examflow.backend.dto.StudentResponse;
import com.examflow.backend.entity.ClassPaymentRecord;
import com.examflow.backend.entity.Classes;
import com.examflow.backend.entity.Student;
import com.examflow.backend.entity.StudentClass;
import com.examflow.backend.entity.StudentClassPaymentRecord;
import com.examflow.backend.entity.UplaodPaper;
import com.examflow.backend.repository.ClassPaymentRecordRepository;
import com.examflow.backend.repository.ClassesRepository;
import com.examflow.backend.repository.StudentClassPaymentRecordsRepository;
import com.examflow.backend.repository.StudentRepository;
import com.examflow.backend.repository.UploadPaperRepository;
import com.examflow.backend.service.StudentControllerManager;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StudentControllerManagerImpl implements StudentControllerManager {

    private final StudentClassesRepository studentClassesRepository;
    private final StudentRepository studentRepository;
    private final UploadPaperRepository uploadPaperRepository;
    private final ClassesRepository classesRepository;
    private final ClassPaymentRecordRepository classPaymentRecordRepository;
    private final StudentClassPaymentRecordsRepository studentClassPaymentRecordsRepository;
    private HttpServletRequest request;

    @Autowired
    public StudentControllerManagerImpl(StudentRepository studentRepository,
            ClassesRepository classesRepository,
            UploadPaperRepository uploadPaperRepository,
            StudentClassPaymentRecordsRepository studentClassPaymentRecordsRepository,
            ClassPaymentRecordRepository classPaymentRecordRepository,
            HttpServletRequest request,
            StudentClassesRepository studentClassesRepository) {
        this.studentRepository = studentRepository;
        this.classesRepository = classesRepository;
        this.uploadPaperRepository = uploadPaperRepository;
        this.classPaymentRecordRepository = classPaymentRecordRepository;
        this.studentClassPaymentRecordsRepository = studentClassPaymentRecordsRepository;
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

    @Override
    public List<ClassResponse> getAllEnrolledClass() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer studentSeq = (Integer) request.getAttribute("userId");
        Student student = studentRepository.findByStudentSeq(studentSeq);
        List<ClassResponse> sendClassResponses = new ArrayList<>();
        List<StudentClass> studentClasses = studentClassesRepository.findByStudentAndStatusSeq(student, 2);

        for (StudentClass studentClass : studentClasses) {
            ClassResponse classResponse = new ClassResponse();
            classResponse.setId(studentClass.getClasses().getClassSeq());
            classResponse.setClass_name(studentClass.getClasses().getDisplayName());
            classResponse.setTeacher_name(studentClass.getClasses().getTutor().getName());
            classResponse.setSubject(studentClass.getClasses().getSubjectName());
            classResponse.setEnrolled_at(studentClass.getCreatedDateTime());
            classResponse.setMonthly_fee(studentClass.getMonthlyFee());
            classResponse.setDescription(studentClass.getClasses().getDescription());

            List<ClassPaymentRecord> classPaymentRecords = classPaymentRecordRepository
                    .findByClassesAndStatus(studentClass.getClasses(), 2);
            List<StudentClassPaymentRecordResponse> studentClassPaymentRecordResponses = new ArrayList<>();
            for (ClassPaymentRecord classPaymentRecord : classPaymentRecords) {

                List<StudentClassPaymentRecord> existstudentClassPaymentRecords = studentClassPaymentRecordsRepository
                        .findByStudentAndClassPaymentRecordAndStatus(student, classPaymentRecord, 2);
                for (StudentClassPaymentRecord studentClassPaymentRecord : existstudentClassPaymentRecords) {
                    StudentClassPaymentRecordResponse clasrep = new StudentClassPaymentRecordResponse();
                    clasrep.setId(studentClassPaymentRecord.getClassPaymentRecord().getClassPaymentRecordSearial());
                    clasrep.setMonth(studentClassPaymentRecord.getClassPaymentRecord().getMonth());
                    clasrep.setYear(studentClassPaymentRecord.getClassPaymentRecord().getYear());
                    if (studentClassPaymentRecord.getIsPayed() == true) {
                        clasrep.setStatus("PAID");
                    } else {
                        clasrep.setStatus("NOT_PAID");
                    }
                    clasrep.setReference_number(studentClassPaymentRecord.getReffrenceNo());
                    clasrep.setPaid_at(studentClassPaymentRecord.getPayedTime());
                    studentClassPaymentRecordResponses.add(clasrep);
                }

            }

            List<ClassPaymentRecord> existsClassPaymentRecords = classPaymentRecordRepository
                    .findByClassesAndStatus(studentClass.getClasses(), 2);
            System.out.println("class payment records" + existsClassPaymentRecords);
            List<MonthPapersResponse> monthPapersResponses = new ArrayList<>();
            for (ClassPaymentRecord classPaymentRecord : existsClassPaymentRecords) {
                MonthPapersResponse newMonthPapersResponse = new MonthPapersResponse();
                newMonthPapersResponse.setMonth(classPaymentRecord.getMonth());
                newMonthPapersResponse.setYear(classPaymentRecord.getYear());
                String monthLable = Month.of(classPaymentRecord.getMonth()).name() + " -  "
                        + classPaymentRecord.getYear();
                newMonthPapersResponse.setMonth_label(monthLable);

                List<UplaodPaper> monthUploadPapers = uploadPaperRepository
                        .findByClassesAndStatusAndClassPaymentRecord(studentClass.getClasses(), 2, classPaymentRecord);
                System.out.println(monthUploadPapers);
                List<PaperResponse> monthpaperResponses = new ArrayList<>();
                for (UplaodPaper monthUploadPaper : monthUploadPapers) {

                    PaperResponse paperResponse = new PaperResponse();
                    paperResponse.setId(monthUploadPaper.getUploadPaperSeq() + " - " + "PAPER");
                    paperResponse.setPaper_name(monthUploadPaper.getPaperName());
                    paperResponse.setDue_date(LocalDateTime.now());
                    paperResponse.setSubmission_status("NOT_SUBMITTED");
                    paperResponse.setGrade("need to implement");
                    paperResponse.setExam_pdf_url(monthUploadPaper.getPaperName());
                    paperResponse.setSubmission_url(monthUploadPaper.getPaperName());
                    paperResponse.setGraded_pdf_url(monthUploadPaper.getPaperName());
                    paperResponse.setIs_current(null);

                    monthpaperResponses.add(paperResponse);

                }
                newMonthPapersResponse.setPapers(monthpaperResponses);
                monthPapersResponses.add(newMonthPapersResponse);
            }

            classResponse.setPapers_by_month(monthPapersResponses);

            classResponse.setMonthly_payments(studentClassPaymentRecordResponses);
            sendClassResponses.add(classResponse);
        }

        return sendClassResponses;
    }

}
