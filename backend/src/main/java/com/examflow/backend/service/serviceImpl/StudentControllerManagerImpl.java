package com.examflow.backend.service.serviceImpl;

import com.examflow.backend.repository.StudentClassesRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import com.examflow.backend.dto.AnswerSheetUploadRequest;
import com.examflow.backend.dto.ClassResponse;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.MonthPapersResponse;
import com.examflow.backend.dto.PaperResponse;
import com.examflow.backend.dto.StudentClassPaymentRecordResponse;
import com.examflow.backend.dto.StudentResponse;
import com.examflow.backend.entity.ClassPaymentRecord;
import com.examflow.backend.entity.Classes;
import com.examflow.backend.entity.PaperSubmission;
import com.examflow.backend.entity.Student;
import com.examflow.backend.entity.StudentClass;
import com.examflow.backend.entity.StudentClassPaymentRecord;
import com.examflow.backend.entity.StudentSubmissionPaperQuestion;
import com.examflow.backend.entity.StudentSubmissionPaperSubQuestion;
import com.examflow.backend.entity.UplaodPaper;
import com.examflow.backend.entity.UploadPaperQuestion;
import com.examflow.backend.entity.UploadPaperQuestionSubQuestion;
import com.examflow.backend.enums.paymentStatus;
import com.examflow.backend.repository.ClassPaymentRecordRepository;
import com.examflow.backend.repository.ClassesRepository;
import com.examflow.backend.repository.PaperSubmissionRepository;
import com.examflow.backend.repository.StudentClassPaymentRecordsRepository;
import com.examflow.backend.repository.StudentRepository;
import com.examflow.backend.repository.StudentSubmissionPaperQuestionRepository;
import com.examflow.backend.repository.StudentSubmissionPaperQuestionSubQuestionRepository;
import com.examflow.backend.repository.UploadPaperQuestionRepository;
import com.examflow.backend.repository.UploadPaperQuestionSubQuestionRepository;
import com.examflow.backend.repository.UploadPaperRepository;
import com.examflow.backend.service.FileStorageService;
import com.examflow.backend.service.StudentControllerManager;
import com.examflow.backend.dto.UserSignUpRequest;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StudentControllerManagerImpl implements StudentControllerManager {

    private final StudentClassesRepository studentClassesRepository;
    private final StudentSubmissionPaperQuestionSubQuestionRepository studentSubmissionPaperQuestionSubQuestionRepository;
    private final UploadPaperQuestionRepository uploadPaperQuestionRepository;
    private final FileStorageService fileStorageService;
    private final PaperSubmissionRepository paperSubmissionRepository;
    private final MonthlyPaymentService monthlyPaymentService;
    private final StudentSubmissionPaperQuestionRepository studentSubmissionPaperQuestionRepository;
    private final StudentRepository studentRepository;
    private final UploadPaperRepository uploadPaperRepository;
    private final UploadPaperQuestionSubQuestionRepository uploadPaperQuestionSubQuestionRepository;
    private final ClassesRepository classesRepository;
    private final ClassPaymentRecordRepository classPaymentRecordRepository;
    private final StudentClassPaymentRecordsRepository studentClassPaymentRecordsRepository;
    private HttpServletRequest request;

    @Autowired
    public StudentControllerManagerImpl(StudentRepository studentRepository,
            ClassesRepository classesRepository,
            StudentSubmissionPaperQuestionSubQuestionRepository studentSubmissionPaperQuestionSubQuestionRepository,
            UploadPaperQuestionRepository uploadPaperQuestionRepository,
            StudentSubmissionPaperQuestionRepository studentSubmissionPaperQuestionRepository,
            PaperSubmissionRepository paperSubmissionRepository,
            UploadPaperQuestionSubQuestionRepository uploadPaperQuestionSubQuestionRepository,
            FileStorageService fileStorageService,
            UploadPaperRepository uploadPaperRepository,
            StudentClassPaymentRecordsRepository studentClassPaymentRecordsRepository,
            ClassPaymentRecordRepository classPaymentRecordRepository,
            MonthlyPaymentService monthlyPaymentService,
            HttpServletRequest request,
            StudentClassesRepository studentClassesRepository) {
        this.studentRepository = studentRepository;
        this.classesRepository = classesRepository;
        this.studentSubmissionPaperQuestionRepository = studentSubmissionPaperQuestionRepository;
        this.studentSubmissionPaperQuestionSubQuestionRepository = studentSubmissionPaperQuestionSubQuestionRepository;
        this.uploadPaperQuestionRepository = uploadPaperQuestionRepository;
        this.paperSubmissionRepository = paperSubmissionRepository;
        this.uploadPaperQuestionSubQuestionRepository = uploadPaperQuestionSubQuestionRepository;
        this.fileStorageService = fileStorageService;
        this.monthlyPaymentService = monthlyPaymentService;
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
            studentResponse.setStudent_number(student.getStudentNo());
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
            monthlyPaymentService.generateStudentClassPaymentRecord();

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
                    if (!Boolean.TRUE.equals(studentClassPaymentRecord.getIsForPayments())) {
                        // No submission on the current active record yet. It may still be a genuinely
                        // untouched month (NOT_PAID), or it may be the fresh placeholder that
                        // rejectPayment()
                        // creates after rejecting a prior submission - check for that prior rejection
                        // so the
                        // student still sees why they were rejected until they resubmit.
                        StudentClassPaymentRecord lastRejected = studentClassPaymentRecordsRepository
                                .findTopByStudentAndClassPaymentRecordAndStatusOrderByApprovedTimeDesc(
                                        student, classPaymentRecord,
                                        paymentStatus.fromStatusName("REJECTED").getSequence());
                        if (lastRejected != null) {
                            clasrep.setStatus("REJECTED");
                            clasrep.setRejection_reason(lastRejected.getReson());
                        } else {
                            clasrep.setStatus("NOT_PAID");
                        }
                    } else if (studentClassPaymentRecord.getIsApproved() == null) {
                        clasrep.setStatus("PENDING");
                    } else if (studentClassPaymentRecord.getIsApproved() == true) {
                        clasrep.setStatus("PAID");
                    } else {
                        clasrep.setStatus("REJECTED");
                        clasrep.setRejection_reason(studentClassPaymentRecord.getReson());
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
                    paperResponse.setId(String.valueOf(monthUploadPaper.getUploadPaperSeq()));
                    paperResponse.setPaper_name(monthUploadPaper.getPaperName());
                    paperResponse.setDue_date(LocalDateTime.now());

                    PaperSubmission paperSubmission = paperSubmissionRepository
                            .findByStudentAndUplaodpaperAndStatusSeq(student, monthUploadPaper, 2);

                    if (paperSubmission != null) {
                        paperResponse.setSubmission_status("SUBMITTED");
                        String pdfUrl = "/uploads/" + paperSubmission.getSubmissionFilePath();
                        paperResponse.setSubmission_url(pdfUrl);
                    } else {
                        paperResponse.setSubmission_status("NOT_SUBMITTED");
                        paperResponse.setSubmission_url(null);
                    }
                    paperResponse.setGrade("need to implement");
                    String pdfUrl = "/uploads/" + monthUploadPaper.getFilePath();
                    paperResponse.setExam_pdf_url(pdfUrl);
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

    @Override
    public StudentResponse getStudentById(Integer id) {
        Student student = studentRepository.findByStudentSeq(id);
        if (student == null)
            return null;

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
        studentResponse.setGuardian_name(student.getGuardianName());
        studentResponse.setGuardian_contact(student.getGuardianContactNumber());
        studentResponse.setAddress(student.getAddress());
        studentResponse.setDistrict(student.getDistrict());
        if (student.getStatus() == 2) {
            studentResponse.setStatus("ACTIVE");
        } else {
            studentResponse.setStatus("INACTIVE");
        }
        return studentResponse;
    }

    @Override
    public GeneralResponse updateStudent(Integer id, UserSignUpRequest studentRequest) {
        GeneralResponse response = new GeneralResponse();
        Student student = studentRepository.findByStudentSeq(id);
        if (student == null) {
            response.setIsSuccess(false);
            response.setMessage("Student not found");
            return response;
        }
        student.setFirstName(studentRequest.getFirstName());
        student.setLastName(studentRequest.getLastName());
        student.setEmail(studentRequest.getEmail());
        student.setContactNumber(studentRequest.getContactNumber());
        student.setWhatsappNumber(studentRequest.getWhatsappNumber());
        student.setSchoolName(studentRequest.getSchoolName());
        student.setGrade(studentRequest.getGrade());
        student.setSubjectStream(studentRequest.getSubjectStream());
        student.setGuardianName(studentRequest.getGuardianName());
        student.setGuardianContactNumber(studentRequest.getGuardianContactNumber());
        student.setAddress(studentRequest.getAddress());
        student.setDistrict(studentRequest.getDistrict());
        studentRepository.save(student);
        response.setIsSuccess(true);
        response.setMessage("Student updated successfully");
        return response;
    }

    @Override
    public GeneralResponse uploadAnswerSheet(AnswerSheetUploadRequest answerSheetUploadRequest,
            MultipartFile answerSheet) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Integer studentSeq = (Integer) request.getAttribute("userId");
        GeneralResponse response = new GeneralResponse();

        Student student = studentRepository.findByStudentSeq(studentSeq);
        UplaodPaper uploadPaper = uploadPaperRepository.findByUploadPaperSeq(answerSheetUploadRequest.getPaperId());

        PaperSubmission paperSubmission = new PaperSubmission();
        paperSubmission.setStudent(student);
        paperSubmission.setUplaodpaper(uploadPaper);
        paperSubmission.setSubmissionBy(username);
        paperSubmission.setSubmissionDate(LocalDateTime.now());
        paperSubmission.setGraded(false);

        String filePath = fileStorageService.saveAnswerSheet(answerSheet);
        paperSubmission.setSubmissionFilePath(filePath);
        paperSubmission.setStatusSeq(2);

        paperSubmissionRepository.save(paperSubmission);

        response.setIsSuccess(true);
        response.setMessage("Answer sheet uploaded successfully");

        List<UploadPaperQuestion> paperQuestions = uploadPaperQuestionRepository.findByUplaodPaperAndStatus(uploadPaper, 2);
        
        for(UploadPaperQuestion question : paperQuestions){
            StudentSubmissionPaperQuestion studentSubmissionPaperQuestion = new StudentSubmissionPaperQuestion();
            UploadPaperQuestion uploadPaperQuestion = uploadPaperQuestionRepository.findByUploadPaperQuestionSeq(question.getUploadPaperQuestionSeq());
            studentSubmissionPaperQuestion.setStudent(student);
            studentSubmissionPaperQuestion.setUploadPaperQuestion(uploadPaperQuestion);
            studentSubmissionPaperQuestion.setStatusSeq(2);
            studentSubmissionPaperQuestionRepository.save(studentSubmissionPaperQuestion);  
            
            List<UploadPaperQuestionSubQuestion> paperSubQuestions = uploadPaperQuestionSubQuestionRepository.findByUploadPaperQuestionAndStatus(question,2);
            if (paperSubQuestions.size()>0) {
                for (UploadPaperQuestionSubQuestion papersubQuestions : paperSubQuestions) {
                StudentSubmissionPaperSubQuestion studentSubQuestion = new StudentSubmissionPaperSubQuestion();
                studentSubQuestion.setStudentSubmissionPaperQuestion(studentSubmissionPaperQuestion);
                studentSubQuestion.setStatusSeq(2);
                studentSubQuestion.setUploadPaperQuestionSubQuestion(papersubQuestions);
                studentSubmissionPaperQuestionSubQuestionRepository.save(studentSubQuestion);
                }
            } 

         }


        return response;

    }

    @Override
    public GeneralResponse removeClassFromStudent(Integer classId) {
        GeneralResponse response = new GeneralResponse();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Integer studentSeq = (Integer) request.getAttribute("userId");

        Student student = studentRepository.findByStudentSeq(studentSeq);
        if (student == null) {
            response.setIsSuccess(false);
            response.setMessage("Student not found");
            return response;
        }

        Classes classes = classesRepository.findByClassSeqAndStatus(classId, 2);
        if (classes == null) {
            response.setIsSuccess(false);
            response.setMessage("Class not found");
            return response;
        }

        StudentClass studentClass = studentClassesRepository.findByStudentAndClassesAndStatusSeq(student, classes, 2);
        if (studentClass == null) {
            response.setIsSuccess(false);
            response.setMessage("Enrollment not found");
            return response;
        }

        studentClass.setStatusSeq(1);
        studentClass.setLastModifiedBy(username);
        studentClass.setLastModifiedDateTime(LocalDateTime.now());
        studentClassesRepository.save(studentClass);

        response.setIsSuccess(true);
        response.setMessage("Class removed successfully");
        return response;
    }
}
