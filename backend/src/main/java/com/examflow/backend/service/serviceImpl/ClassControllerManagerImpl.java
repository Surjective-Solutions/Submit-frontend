package com.examflow.backend.service.serviceImpl;

import java.time.LocalDateTime;
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
import com.examflow.backend.dto.UploadPaperResponse;
import com.examflow.backend.entity.Cashier;
import com.examflow.backend.entity.ClassPaymentRecord;
import com.examflow.backend.entity.Classes;
import com.examflow.backend.entity.Tutor;
import com.examflow.backend.entity.UplaodPaper;
import com.examflow.backend.repository.ClassPaymentRecordRepository;
import com.examflow.backend.repository.ClassesRepository;
import com.examflow.backend.repository.TutorRepository;
import com.examflow.backend.repository.UploadPaperRepository;
import com.examflow.backend.service.ClassControllerManager;
import com.examflow.backend.service.FileStorageService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ClassControllerManagerImpl implements ClassControllerManager {

    private HttpServletRequest request;
    private final TutorRepository tutorRepository;
    private final FileStorageService fileStorageService;
    private final ClassesRepository classesRepository;
    private final UploadPaperRepository uploadPaperRepository;
    private final ClassPaymentRecordRepository classPaymentRecordRepository;

    @Autowired
    public ClassControllerManagerImpl(HttpServletRequest request,
            TutorRepository tutorRepository,
            FileStorageService fileStorageService,
            ClassPaymentRecordRepository classPaymentRecordRepository,
            ClassesRepository classesRepository,
            UploadPaperRepository uploadPaperRepository) {
        this.request = request;
        this.tutorRepository = tutorRepository;
        this.fileStorageService = fileStorageService;
        this.classPaymentRecordRepository = classPaymentRecordRepository;
        this.classesRepository = classesRepository;
        this.uploadPaperRepository = uploadPaperRepository;
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

        return response;
    }

    @Override
    public List<ClassResponse> getAllClasses() {
        System.out.println("reached to cimpl");
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        GeneralResponse response = new GeneralResponse();
        Classes classes = classesRepository.findByClassSeqAndStatus(classId, 2);
        System.out.println(classId);
        ClassPaymentRecord classPaymentRecord = classPaymentRecordRepository.findByClassesAndStatusAndMonth(classes, 2,
                paperUploadRequest.getMonth());
        if (classes == null) {
            response.setMessage("Class not found");
            response.setIsSuccess(false);

            return response;
        }

        if (classPaymentRecord == null) {

            response.setIsSuccess(false);
            response.setMessage("make payment record to respective month and class");
            return response;
        }

        // save to local storage.
        String fileName = fileStorageService.savePaperFile(pdf_file);

        UplaodPaper uploadPaper = new UplaodPaper();
        uploadPaper.setPaperName(paperUploadRequest.getPaper_name());
        uploadPaper.setMonth(paperUploadRequest.getMonth().toString());
        uploadPaper.setYear(paperUploadRequest.getYear().toString());
        uploadPaper.setNoOfQuestions(paperUploadRequest.getNumber_of_questions());
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

        response.setMessage("Paper uploaded successfully");
        response.setIsSuccess(true);
        System.out.println("Paper uploaded successfully");

        return response;
    }

}
