package com.examflow.backend.service.serviceImpl;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.examflow.backend.dto.CashierResponse;
import com.examflow.backend.dto.ClassRequest;
import com.examflow.backend.dto.ClassResponse;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.MonthPapersResponse;
import com.examflow.backend.dto.PaperResponse;
import com.examflow.backend.dto.PaperUploadRequest;
import com.examflow.backend.dto.QuestionRequestDTO;
import com.examflow.backend.dto.SubQuestionRequestDTO;
import com.examflow.backend.dto.UploadPaperResponse;
import com.examflow.backend.entity.Cashier;
import com.examflow.backend.entity.ClassPaymentRecord;
import com.examflow.backend.entity.Classes;
import com.examflow.backend.entity.Tutor;
import com.examflow.backend.entity.UplaodPaper;
import com.examflow.backend.entity.UploadPaperQuestion;
import com.examflow.backend.entity.UploadPaperQuestionSubQuestion;
import com.examflow.backend.repository.ClassPaymentRecordRepository;
import com.examflow.backend.repository.ClassesRepository;
import com.examflow.backend.repository.TutorRepository;
import com.examflow.backend.repository.UploadPaperQuestionRepository;
import com.examflow.backend.repository.UploadPaperQuestionSubQuestionRepository;
import com.examflow.backend.repository.UploadPaperRepository;
import com.examflow.backend.service.ClassControllerManager;
import com.examflow.backend.service.FileStorageService;


import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
public class ClassControllerManagerImpl implements ClassControllerManager {

    private HttpServletRequest request;
    private final TutorRepository tutorRepository;
    private final FileStorageService fileStorageService;
    private final ClassesRepository classesRepository;
    private final UploadPaperRepository uploadPaperRepository;
    private final ClassPaymentRecordRepository classPaymentRecordRepository;
    private final UploadPaperQuestionRepository uploadPaperQuestionRepository;
    private final UploadPaperQuestionSubQuestionRepository uploadPaperQuestionSubQuestionRepository;
    private final MonthlyPaymentService monthlyPaymentService;

    @Autowired
    public ClassControllerManagerImpl(HttpServletRequest request,
            TutorRepository tutorRepository,
            UploadPaperQuestionRepository uploadPaperQuestionRepository,
            UploadPaperQuestionSubQuestionRepository uploadPaperQuestionSubQuestionRepository,
            FileStorageService fileStorageService,
            ClassPaymentRecordRepository classPaymentRecordRepository,
            ClassesRepository classesRepository,
            UploadPaperRepository uploadPaperRepository,
            MonthlyPaymentService monthlyPaymentService) {
        this.request = request;
        this.tutorRepository = tutorRepository;
        this.fileStorageService = fileStorageService;
        this.classPaymentRecordRepository = classPaymentRecordRepository;
        this.classesRepository = classesRepository;
        this.uploadPaperQuestionRepository = uploadPaperQuestionRepository;
        this.uploadPaperQuestionSubQuestionRepository = uploadPaperQuestionSubQuestionRepository;
        this.uploadPaperRepository = uploadPaperRepository;
        this.monthlyPaymentService = monthlyPaymentService;
    }

    @Override
    public GeneralResponse createClass(ClassRequest classRequest) {
        System.out.println("reached to impl");
        GeneralResponse response = new GeneralResponse();
        Classes newClass = new Classes();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Integer tutorSeq = (Integer) request.getAttribute("userId");
        Tutor tutor = tutorRepository.findByTutorSeq(tutorSeq);

        newClass.setDisplayName(classRequest.getDisplay_name());
        newClass.setDescription(classRequest.getDescription());
        newClass.setTutor(tutor);
        newClass.setMonthlyFee(classRequest.getMonthly_fee());
        newClass.setSubjectName(classRequest.getSubject_name());
        newClass.setLastModifiedBy(username);
        newClass.setCreatedBy(username);
        newClass.setLastModifiedDateTime(LocalDateTime.now());
        newClass.setCreatedDateTime(LocalDateTime.now());
        newClass.setStatus(2); // Set status to 2 for active

        classesRepository.save(newClass);

        response.setMessage("Class created successfully");
        response.setIsSuccess(true);
        System.out.println("Class created successfully");

        try {
            ClassPaymentRecord classPaymentRecord = new ClassPaymentRecord();
            YearMonth month = YearMonth.of(newClass.getCreatedDateTime().getYear(),
                    newClass.getCreatedDateTime().getMonth());
            Integer monthnumber = month.getMonthValue();
            Integer yearnumber = month.getYear();

            classPaymentRecord.setClasses(newClass);
            classPaymentRecord.setMonth(monthnumber);
            classPaymentRecord.setYear(yearnumber);
            classPaymentRecord.setStatus(2);
            classPaymentRecord.setCreatedBy("SYSTEM");
            classPaymentRecord.setLastModifiedBy("SYSTEM");
            classPaymentRecord.setCreatedDateTime(LocalDateTime.now());
            classPaymentRecord.setLastModifiedDateTime(LocalDateTime.now());
            classPaymentRecord.setClassPaymentRecordSearial(monthnumber + "-" + yearnumber + '-' + "payment");
            classPaymentRecordRepository.save(classPaymentRecord);

        } catch (Exception e) {

        }

        return response;
    }

    @Override
    public List<ClassResponse> getAllClasses() {
        System.out.println("reached to impl");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Integer tutorSeq = (Integer) request.getAttribute("userId");
        Tutor tutor = tutorRepository.findByTutorSeq(tutorSeq);
        List<Classes> classList = classesRepository.findByStatusAndTutor(2, tutor);

        List<ClassResponse> classResponseList = new ArrayList<>();

        for (Classes classes : classList) {
            ClassResponse classResponse = new ClassResponse();
            classResponse.setDisplay_name(classes.getDisplayName());
            classResponse.setDescription(classes.getDescription());
            classResponse.setMonthly_fee(classes.getMonthlyFee());
            classResponse.setSubject_name(classes.getSubjectName());
            classResponse.setId(classes.getClassSeq());

            List<UplaodPaper> papers = uploadPaperRepository.findByClassesAndStatus(classes, 2);

            List<UploadPaperResponse> paperResponses = new ArrayList<>();
            for (UplaodPaper paper : papers) {
                UploadPaperResponse paperResponse = new UploadPaperResponse();
                paperResponse.setPaper_name(paper.getPaperName());
                paperResponse.setId(paper.getUploadPaperSeq());
                paperResponse.setMonth(Integer.valueOf(paper.getMonth()));
                paperResponse.setYear(Integer.valueOf(paper.getYear()));
                paperResponse.setNumber_of_questions(paper.getNoOfQuestions());
                paperResponse.setUploaded_at(paper.getCreatedDateTime());
                paperResponse.setPdf_url(paper.getFileName());

                if (paper.getIsPublished() == true) {
                    paperResponse.setStatus("PUBLISHED");

                } else {
                    paperResponse.setStatus("DRAFT");
                }

                paperResponses.add(paperResponse);
            }

            classResponse.setPapers(paperResponses);

            classResponseList.add(classResponse);
        }

        return classResponseList;
    }

    @Override
    public GeneralResponse uploadPapers(PaperUploadRequest paperUploadRequest, Integer classId,
            MultipartFile pdf_file) {
        ObjectMapper mapper = new ObjectMapper();
        List<QuestionRequestDTO> questions = mapper.readValue(
                paperUploadRequest.getQuestions(),
                new TypeReference<List<QuestionRequestDTO>>() {
                });

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        GeneralResponse response = new GeneralResponse();
        Classes classes = classesRepository.findByClassSeqAndStatus(classId, 2);
        System.out.println(classId);
        if (classes == null) {
            response.setMessage("Class not found");
            response.setIsSuccess(false);

            return response;
        }

        ClassPaymentRecord classPaymentRecord = monthlyPaymentService.getOrCreateClassPaymentRecord(classes,
                paperUploadRequest.getMonth(), paperUploadRequest.getYear(), username);

        // save to local storage.
        String fileName = fileStorageService.savePaperFile(pdf_file);

        UplaodPaper uploadPaper = new UplaodPaper();
        uploadPaper.setPaperName(paperUploadRequest.getPaper_name());
        uploadPaper.setMonth(paperUploadRequest.getMonth().toString());
        uploadPaper.setYear(paperUploadRequest.getYear().toString());
        // uploadPaper.setNoOfQuestions(paperUploadRequest.getNumber_of_questions());
        uploadPaper.setCreatedBy(username);
        uploadPaper.setLastModifiedBy(username);
        uploadPaper.setFileName(fileName);
        uploadPaper.setFilePath(System.getProperty("user.home")
                + "/lms/uploads/papers/" + fileName);
        uploadPaper.setCreatedDateTime(LocalDateTime.now());

        uploadPaper.setLastModifiedDateTime(LocalDateTime.now());
        uploadPaper.setStatus(2);

        if (paperUploadRequest.getStatus() == "DRAFT") {

            uploadPaper.setIsPublished(false);

        } else {

            uploadPaper.setIsPublished(true);
        }
        uploadPaper.setClasses(classes);
        uploadPaper.setClassPaymentRecord(classPaymentRecord);

        uploadPaperRepository.save(uploadPaper);
        System.out.println("Paper uploaded successfully");
        Integer numberOfQuetions = 0;
        for (QuestionRequestDTO question : questions) {
            UploadPaperQuestion uploadPaperQuestion = new UploadPaperQuestion();
            numberOfQuetions++;
            uploadPaperQuestion.setMarks(question.getMarks());
            uploadPaperQuestion.setQuestionKey(question.getKey());
            uploadPaperQuestion.setUplaodPaper(uploadPaper);
            uploadPaperQuestion.setStatus(2);
            uploadPaperQuestion.setCreatedDateTime(LocalDateTime.now());
            uploadPaperQuestion.setLastModifiedDateTime(LocalDateTime.now());
            uploadPaperQuestion.setCreatedBy(username);
            uploadPaperQuestion.setLastModifiedBy(username);

            uploadPaperQuestionRepository.save(uploadPaperQuestion);

            System.out.println("Question key: " + question.getKey() + "created.");
            System.out.println("Marks: " + question.getMarks());

            Integer totalSubQuestionMarks = 0;
            for (SubQuestionRequestDTO subpart : question.getSubparts()) {
                UploadPaperQuestionSubQuestion uploadPaperQuestionSubQuestion = new UploadPaperQuestionSubQuestion();

                uploadPaperQuestionSubQuestion.setMark(subpart.getMarks());
                totalSubQuestionMarks = +subpart.getMarks();
                uploadPaperQuestionSubQuestion.setQuestionKey(subpart.getKey());
                uploadPaperQuestionSubQuestion.setUploadPaperQuestion(uploadPaperQuestion);
                uploadPaperQuestionSubQuestion.setStatus(2);
                uploadPaperQuestionSubQuestion.setCreatedDateTime(LocalDateTime.now());
                uploadPaperQuestionSubQuestion.setLastModifiedDateTime(LocalDateTime.now());
                uploadPaperQuestionSubQuestion.setCreatedBy(username);
                uploadPaperQuestionSubQuestion.setLastModifiedBy(username);

                uploadPaperQuestionSubQuestionRepository.save(uploadPaperQuestionSubQuestion);
                System.out.println(
                        "Subpart key: " + subpart.getKey()
                                + " Marks: " + subpart.getMarks() + "created");
            }
            if (uploadPaperQuestion.getMarks() == null) {
                uploadPaperQuestion.setMarks(totalSubQuestionMarks);
                uploadPaperQuestionRepository.save(uploadPaperQuestion);
            }

        }

        uploadPaper.setNoOfQuestions(numberOfQuetions);
        uploadPaperRepository.save(uploadPaper);

        response.setMessage("Paper uploaded successfully");
        response.setIsSuccess(true);


        return response;
    }

    @Override
    public GeneralResponse updateClass(Integer classId, ClassRequest classRequest) {
        GeneralResponse response = new GeneralResponse();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Classes classes = classesRepository.findByClassSeqAndStatus(classId, 2);

        if (classes == null) {
            response.setIsSuccess(false);
            response.setMessage("Class not found");
            return response;
        }

        classes.setDisplayName(classRequest.getDisplay_name());
        classes.setDescription(classRequest.getDescription());
        classes.setMonthlyFee(classRequest.getMonthly_fee());
        classes.setSubjectName(classRequest.getSubject_name());
        classes.setLastModifiedBy(username);
        classes.setLastModifiedDateTime(LocalDateTime.now());

        classesRepository.save(classes);
        response.setMessage("Class updated successfully");
        response.setIsSuccess(true);
        return response;
    }
}
