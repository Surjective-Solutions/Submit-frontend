package com.examflow.backend.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.examflow.backend.dto.CashierResponse;
import com.examflow.backend.dto.InstructorResponse;
import com.examflow.backend.entity.Cashier;
import com.examflow.backend.entity.Classes;
import com.examflow.backend.entity.GradeSubmission;
import com.examflow.backend.entity.GradeSubmissionQuestion;

import jakarta.servlet.http.HttpServletRequest;

import com.examflow.backend.entity.Instructor;
import com.examflow.backend.entity.PaperSubmission;
import com.examflow.backend.entity.Student;
import com.examflow.backend.entity.TutorInstructor;
import com.examflow.backend.entity.UplaodPaper;
import com.examflow.backend.entity.UploadPaperQuestion;
import com.examflow.backend.entity.UploadPaperQuestionSubQuestion;
import com.examflow.backend.repository.ClassesRepository;
import com.examflow.backend.repository.GradeSubmissionQuestionRepository;
import com.examflow.backend.repository.GradeSubmissionRepository;
import com.examflow.backend.repository.InstructorRepository;
import com.examflow.backend.repository.PaperSubmissionRepository;
import com.examflow.backend.repository.TutorInstructorRepository;
import com.examflow.backend.repository.UploadPaperQuestionRepository;
import com.examflow.backend.repository.UploadPaperQuestionSubQuestionRepository;
import com.examflow.backend.repository.UploadPaperRepository;
import com.examflow.backend.service.InstructorControllerManager;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.InstructorSignUpRequest;
import com.examflow.backend.dto.InstructorTeacherClassesResponse;
import com.examflow.backend.dto.InstructorTeacherResponse;
import com.examflow.backend.dto.PaperInstructorTutorResponse;
import com.examflow.backend.dto.QuestionPaperInstructorTutorResponse;
import com.examflow.backend.dto.SubmissionPaperInstructorTutorResponse;
import com.examflow.backend.dto.SubmitGradeQuestionsResponse;
import com.examflow.backend.dto.SubmitGradeResponse;

@Service
public class InstructorControllerManagerImpl implements InstructorControllerManager {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UploadPaperQuestionRepository uploadPaperQuestionRepository;
    private final UploadPaperQuestionSubQuestionRepository uploadPaperQuestionSubQuestionRepository;
    private final ClassesRepository classesRepository;
    private final PaperSubmissionRepository paperSubmissionRepository;
    private final GradeSubmissionQuestionRepository gradeSubmissionQuestionRepository;
    private final GradeSubmissionRepository gradeSubmissionRepository;
    private final UploadPaperRepository uploadPaperRepository;
    private final TutorInstructorRepository tutorInstructorRepository;
    private HttpServletRequest request;
    private final InstructorRepository instructorRepository;

    @Autowired
    public InstructorControllerManagerImpl(InstructorRepository instructorRepository,
            BCryptPasswordEncoder passwordEncoder,
            UploadPaperRepository uploadPaperRepository,
            PaperSubmissionRepository paperSubmissionRepository,
            UploadPaperQuestionSubQuestionRepository uploadPaperQuestionSubQuestionRepository,
            ClassesRepository classesRepository,
            GradeSubmissionRepository gradeSubmissionRepository,
            GradeSubmissionQuestionRepository gradeSubmissionQuestionRepository,
            TutorInstructorRepository tutorInstructorRepository,
            UploadPaperQuestionRepository uploadPaperQuestionRepository,
            HttpServletRequest request) {
        this.instructorRepository = instructorRepository;
        this.paperSubmissionRepository = paperSubmissionRepository;
        this.gradeSubmissionQuestionRepository = gradeSubmissionQuestionRepository;
        this.gradeSubmissionRepository = gradeSubmissionRepository;
        this.uploadPaperRepository = uploadPaperRepository;
        this.uploadPaperQuestionSubQuestionRepository = uploadPaperQuestionSubQuestionRepository;
        this.uploadPaperQuestionRepository = uploadPaperQuestionRepository;
        this.classesRepository = classesRepository;
        this.passwordEncoder = passwordEncoder;
        this.tutorInstructorRepository = tutorInstructorRepository;
        this.request = request;
    }

    @Override
    public List<InstructorResponse> getAllInstructors() {

        List<Instructor> instructors = instructorRepository.findByStatusNot(0);

        List<InstructorResponse> instructorResponseList = new ArrayList<>();

        for (Instructor instructor : instructors) {

            InstructorResponse instructorResponse = new InstructorResponse();
            String fullName = instructor.getFullName();

            if (fullName != null && !fullName.isBlank()) {
                int lastSpaceIndex = fullName.lastIndexOf(" ");

                if (lastSpaceIndex > 0) {
                    instructorResponse.setFirst_name(
                            fullName.substring(0, lastSpaceIndex));

                    instructorResponse.setLast_name(
                            fullName.substring(lastSpaceIndex + 1));
                } else {
                    // Only one name exists
                    instructorResponse.setFirst_name(fullName);
                    instructorResponse.setLast_name("");
                }
            }

            instructorResponse.setId(instructor.getInstructorSeq());
            instructorResponse.setEmployee_id(instructor.getInstrutorNo());
            instructorResponse.setEmail(instructor.getEmail());
            instructorResponse.setContact_number(instructor.getContactNumber());
            instructorResponse.setSubject_area(instructor.getSubjectArea());
            instructorResponse.setStatusSeq(instructor.getStatus());
            if (instructor.getStatus() == 2) {
                instructorResponse.setStatus("ACTIVE");
            } else if (instructor.getStatus() == 0) {
                instructorResponse.setStatus("DELETED");

            } else {
                instructorResponse.setStatus("INACTIVE");
            }
            instructorResponse.setProfile_photo_url(null);

            instructorResponseList.add(instructorResponse);
        }

        return instructorResponseList;
    }

    @Override
    public InstructorResponse getInstructorById(Integer id) {
        Instructor instructor = instructorRepository.findByInstructorSeq(id);
        if (instructor == null) return null;

        InstructorResponse instructorResponse = new InstructorResponse();
        instructorResponse.setId(instructor.getInstructorSeq());
        instructorResponse.setEmployee_id(null);
        instructorResponse.setFirst_name(instructor.getFullName());
        instructorResponse.setLast_name(null);
        instructorResponse.setEmail(instructor.getEmail());
        instructorResponse.setContact_number(instructor.getContactNumber());
        instructorResponse.setSubject_area(null);
        instructorResponse.setStatusSeq(instructor.getStatus());
        instructorResponse.setProfile_photo_url(null);

        return instructorResponse;
    }

    @Override
    public GeneralResponse updateInstructor(Integer id, InstructorSignUpRequest instructorRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        GeneralResponse response = new GeneralResponse();
        Instructor instructor = instructorRepository.findByInstructorSeq(id);
        if (instructor == null) {
            response.setIsSuccess(false);
            response.setMessage("Instructor not found");
            return response;
        }
        instructor.setFullName(instructorRequest.getFirstName() + ' ' + instructorRequest.getLastName());
        instructor.setEmail(instructorRequest.getEmail());
        instructor.setInstrutorNo(instructorRequest.getEmployeeId());
        instructor.setContactNumber(instructorRequest.getContactNumber());
        instructor.setLastModifiedBy(username);
        instructor.setLastModifiedDateTime(LocalDateTime.now());
        if (instructorRequest.getStatus() == "ACTIVE") {
            instructor.setStatus(2);
        } else {
            instructor.setStatus(1);
        }
        instructor.setAddress(instructorRequest.getAddress());
        instructor.setSubjectArea(instructorRequest.getSubjectArea());

        if (instructorRequest.getPassword() != null) {
            if (instructorRequest.getPassword().equals(instructorRequest.getConfirmPassword())) {
                instructor.setPassword(passwordEncoder.encode(instructorRequest.getPassword()));
                instructor.setConfirmPassword(passwordEncoder.encode(instructorRequest.getPassword()));
                instructor.setFinalPassword(passwordEncoder.encode(instructorRequest.getPassword()));

            } else {
                response.setIsSuccess(false);
                response.setMessage("Confirmed Password and Password Not equal ");
                return response;
            }
        }

        instructorRepository.save(instructor);
        response.setIsSuccess(true);
        response.setMessage("Instructor updated successfully");
        return response;

    }

    @Override
    public List<InstructorTeacherResponse> getIntructorTeachers() {


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Integer instructorSeq = (Integer) request.getAttribute("userId");
        Instructor instructor = instructorRepository.findByInstructorSeq(instructorSeq);

        List<TutorInstructor> tutorInstructors = tutorInstructorRepository.findByInstructorAndIsEngaged(instructor, true);
        List<InstructorTeacherResponse> responseList = new ArrayList<>();
        for (TutorInstructor tutorInstructor : tutorInstructors) {
            InstructorTeacherResponse response = new InstructorTeacherResponse();
            response.setId(tutorInstructor.getTutor().getTutorSeq().toString());
            response.setTeacher_name(tutorInstructor.getTutor().getName());
            response.setSubject_area(tutorInstructor.getTutor().getSubject());
            response.setEmployee_id(tutorInstructor.getTutor().getTutorSeq().toString());
            response.setBio(null);

            List<Classes> classesList = classesRepository.findByStatusAndTutor(2, tutorInstructor.getTutor());
            List<InstructorTeacherClassesResponse> classesResponseList = new ArrayList<>();
            for (Classes classes : classesList) {
                InstructorTeacherClassesResponse classesResponse = new InstructorTeacherClassesResponse();
                classesResponse.setId(classes.getClassSeq().toString());
                classesResponse.setClass_name(classes.getDisplayName());
                classesResponse.setClass_year("2026");// Assuming class_year is hardcoded for now, you may want to replace this with actual data from the Classes entity
                classesResponse.setSubject(classes.getSubjectName());
                classesResponse.setMonthly_fee(classes.getMonthlyFee().toString());
                if (classes.getStatus() == 2) {
                    classesResponse.setStatus("ACTIVE");
                } else if (classes.getStatus() == 0) {
                    classesResponse.setStatus("DELETED");
                } else {
                    classesResponse.setStatus("INACTIVE");
                }
                classesResponseList.add(classesResponse);
            }

            response.setClasses(classesResponseList);

            List<UplaodPaper> uplaodPapers = uploadPaperRepository.findByClassesTutorAndStatus(tutorInstructor.getTutor(), 2);
            List<PaperInstructorTutorResponse> paperInstructorTutorResponses = new ArrayList<>();

            for(UplaodPaper tutorPapers : uplaodPapers){
                PaperInstructorTutorResponse paper = new PaperInstructorTutorResponse();

                paper.setId(tutorPapers.getUploadPaperSeq().toString());
                paper.setPaper_name(tutorPapers.getPaperName());
                paper.setClass_name(tutorPapers.getClasses().getDisplayName());
                paper.setClass_id(tutorPapers.getClasses().getClassSeq().toString());
                paper.setMonth_label(tutorPapers.getClassPaymentRecord().getMonth().toString());
                paper.setNumber_of_questions(null);
                paper.setUploaded_at(tutorPapers.getCreatedDateTime());

                paperInstructorTutorResponses.add(paper);

                List<UploadPaperQuestion> paperQuestions = uploadPaperQuestionRepository.findByUplaodPaperAndStatusOrderByQuestionKeyAsc(tutorPapers, 2);
                List<QuestionPaperInstructorTutorResponse> questionResponses = new ArrayList<>();
                Integer questionCount = 0;
                for(UploadPaperQuestion paperQuestion : paperQuestions){
                    QuestionPaperInstructorTutorResponse questionResponse = new QuestionPaperInstructorTutorResponse();
                    List<UploadPaperQuestionSubQuestion> subQuestions = uploadPaperQuestionSubQuestionRepository.findByUploadPaperQuestionAndStatusOrderByQuestionKeyAsc(paperQuestion, 2);
                    questionCount++;
                    if (subQuestions.size()==0) {
                        
                        questionResponse.setId(paperQuestion.getUploadPaperQuestionSeq().toString());
                        questionResponse.setMainQuestionSeq(paperQuestion.getUploadPaperQuestionSeq());
                        questionResponse.setQuestion_label("Q" + questionCount.toString());
                        questionResponse.setParent_label(null);
                        questionResponse.setMax_marks(paperQuestion.getMarks());
                        questionResponse.setDisplay_order(paperQuestion.getQuestionKey());
                        questionResponses.add(questionResponse);
                    }else{
                        Integer subQuestionCount = 0;
                        for(UploadPaperQuestionSubQuestion subQuestion : subQuestions){
                            QuestionPaperInstructorTutorResponse subQuestionResponse = new QuestionPaperInstructorTutorResponse();
                            subQuestionCount++;
                            subQuestionResponse.setId(paperQuestion.getUploadPaperQuestionSeq().toString() + "-" + subQuestion.getUploadPaperQuestionSubQuestionSeq().toString());
                            subQuestionResponse.setSubQuestionSeq(subQuestion.getUploadPaperQuestionSubQuestionSeq());
                            subQuestionResponse.setMainQuestionSeq(paperQuestion.getUploadPaperQuestionSeq());
                            subQuestionResponse.setQuestion_label("Q" + questionCount.toString()+"(" + subQuestionCount.toString() + ")");
                            subQuestionResponse.setParent_label("Q" + questionCount.toString());
                            subQuestionResponse.setMax_marks(subQuestion.getMark());
                            subQuestionResponse.setDisplay_order(subQuestion.getQuestionKey());
                            questionResponses.add(subQuestionResponse);
                        }
                    }


                    
                }
                paper.setQuestions(questionResponses);

                List<PaperSubmission> paperSubmissions = paperSubmissionRepository.findByUplaodpaperAndStatusSeq(tutorPapers, 2);
                List<SubmissionPaperInstructorTutorResponse> submissionResponses = new ArrayList<>();
                for(PaperSubmission submission : paperSubmissions){
                    SubmissionPaperInstructorTutorResponse submissionResponse = new SubmissionPaperInstructorTutorResponse();
                    submissionResponse.setId(submission.getPaperSubmissionSeq().toString());
                    submissionResponse.setStudent_name(submission.getStudent().getFirstName() + " " + submission.getStudent().getLastName());
                    submissionResponse.setStudent_number(submission.getStudent().getStudentNo().toString());
                    submissionResponse.setSubmitted_at(submission.getSubmissionDate());
                    submissionResponse.setGraded(submission.isGraded());
                    submissionResponse.setGraded_at(submission.getGradedDate());
                    submissionResponse.setFile_url(submission.getSubmissionFilePath());
                    
                    submissionResponses.add(submissionResponse);
                }

                paper.setSubmissions(submissionResponses);


            }
            response.setPapers(paperInstructorTutorResponses);


            
            responseList.add(response);
        }

        return responseList;

    }



    @Override
    public GeneralResponse GradeSubmission(SubmitGradeResponse submitGradeResponse) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Integer instructorSeq = (Integer) request.getAttribute("userId");
        Instructor instructor = instructorRepository.findByInstructorSeq(instructorSeq);


        GeneralResponse response = new GeneralResponse();

        GradeSubmission gradeSubmission = new GradeSubmission();

        gradeSubmission.setCreatedBy(username);
        gradeSubmission.setLastModifiedBy(username);
        gradeSubmission.setGrardedBy(username);
        gradeSubmission.setCreatedAt(LocalDateTime.now());
        gradeSubmission.setLastModifiedAt(LocalDateTime.now());      
        gradeSubmission.setGradedAt(LocalDateTime.now());   

        


        PaperSubmission paperSubmission = paperSubmissionRepository.findByPaperSubmissionSeq(submitGradeResponse.getSubmissionId());
        if (paperSubmission == null) {
            // Handle the case where the paper submission is not found
           response.setIsSuccess(false);
            response.setMessage("Paper submission not found Contact an System administrator");
            return response;

        }

        gradeSubmission.setPaperSubmission(paperSubmission);
        gradeSubmission.setMaxMarks(submitGradeResponse.getMaxMarks());
        gradeSubmission.setGrade(submitGradeResponse.getGrade());
        gradeSubmission.setTotalMarks(submitGradeResponse.getTotalMarks());
        gradeSubmission.setStatus(2); // make the record Approve status


        gradeSubmissionRepository.save(gradeSubmission);

        

         for(SubmitGradeQuestionsResponse submitGradeQuestionsResponse : submitGradeResponse.getQuestions()){
            GradeSubmissionQuestion gradeSubmissionQuestion = new GradeSubmissionQuestion();
            gradeSubmissionQuestion.setGradeSubmission(gradeSubmission);    
            gradeSubmissionQuestion.setComment(submitGradeQuestionsResponse.getComment());
            gradeSubmissionQuestion.setMarksAwarded(submitGradeQuestionsResponse.getMarksAwarded());
            gradeSubmissionQuestion.setStatus(2);// make the record Approve status
            if (submitGradeQuestionsResponse.getIsSubQuestion() == true) {
                
                UploadPaperQuestionSubQuestion subQuestion = uploadPaperQuestionSubQuestionRepository.findByUploadPaperQuestionSubQuestionSeq(submitGradeQuestionsResponse.getSubquestionSeq());
                if (subQuestion == null) {
                    response.setIsSuccess(false);
                    response.setMessage("Sub Question not found Contact an System administrator");
                    

                    gradeSubmission.setStatus(1);// make the record open status
                    gradeSubmissionRepository.save(gradeSubmission);



                    return response;
                }

                gradeSubmissionQuestion.setIsSubQuestion(true);
                gradeSubmissionQuestion.setUploadPaperQuestionSubQuestion(subQuestion);
                gradeSubmissionQuestion.setUploadPaperQuestion(subQuestion.getUploadPaperQuestion());
            } else {
                UploadPaperQuestion question = uploadPaperQuestionRepository.findByUploadPaperQuestionSeq(submitGradeQuestionsResponse.getMainQuestionSeq());
                if (question == null) {
                    response.setIsSuccess(false);
                    response.setMessage("Question not found Contact an System administrator");


                    gradeSubmission.setStatus(1);// make the record open status
                    gradeSubmissionRepository.save(gradeSubmission);


                    return response;
                }
                gradeSubmissionQuestion.setUploadPaperQuestion(question);
                gradeSubmissionQuestion.setIsSubQuestion(false);
            }
            
            gradeSubmissionQuestionRepository.save(gradeSubmissionQuestion);

         }

         PaperSubmission paperSubmissionToUpdate = gradeSubmission.getPaperSubmission();
         paperSubmissionToUpdate.setGraded(true);
         paperSubmissionToUpdate.setGradedDate(LocalDateTime.now());

        paperSubmissionRepository.save(paperSubmissionToUpdate);

        
          response.setIsSuccess(true);
          response.setMessage("Grading submitted successfully");
          return response;
    }

}
