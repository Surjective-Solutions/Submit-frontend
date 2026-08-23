package com.examflow.backend.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examflow.backend.dto.QuestionGradeResponse;
import com.examflow.backend.dto.RegradeRequestResponse;
import com.examflow.backend.entity.Classes;
import com.examflow.backend.entity.GradeSubmission;
import com.examflow.backend.entity.GradeSubmissionQuestion;
import com.examflow.backend.entity.PaperSubmission;
import com.examflow.backend.entity.RegradeRequest;
import com.examflow.backend.entity.Student;
import com.examflow.backend.entity.Tutor;
import com.examflow.backend.entity.UplaodPaper;
import com.examflow.backend.repository.GradeSubmissionQuestionRepository;
import com.examflow.backend.repository.RegradeRequestRepository;

// Shared by the teacher (single owning tutor) and instructor (every engaged tutor)
// regrade-request views, so "pending requests" and "previous marks" are read the
// same way regardless of which role is looking at them.
@Service
public class RegradeRequestQueryService {

    private final RegradeRequestRepository regradeRequestRepository;
    private final GradeSubmissionQuestionRepository gradeSubmissionQuestionRepository;

    @Autowired
    public RegradeRequestQueryService(RegradeRequestRepository regradeRequestRepository,
            GradeSubmissionQuestionRepository gradeSubmissionQuestionRepository) {
        this.regradeRequestRepository = regradeRequestRepository;
        this.gradeSubmissionQuestionRepository = gradeSubmissionQuestionRepository;
    }

    public List<RegradeRequestResponse> getPendingForTutors(List<Tutor> tutors) {
        List<RegradeRequest> pendingRequests = regradeRequestRepository
                .findByStatusAndPaperSubmission_Uplaodpaper_Classes_TutorInOrderByRequestedAtAsc(1, tutors);

        List<RegradeRequestResponse> responses = new ArrayList<>();
        for (RegradeRequest regradeRequest : pendingRequests) {
            responses.add(buildResponse(regradeRequest));
        }
        return responses;
    }

    public RegradeRequestResponse getByIdForTutors(Integer regradeRequestSeq, List<Tutor> tutors) {
        RegradeRequest regradeRequest = regradeRequestRepository.findByRegradeRequestSeq(regradeRequestSeq);
        if (regradeRequest == null) {
            return null;
        }

        Tutor owningTutor = regradeRequest.getPaperSubmission().getUplaodpaper().getClasses().getTutor();
        boolean authorized = owningTutor != null && tutors.stream()
                .anyMatch(tutor -> tutor != null && tutor.getTutorSeq().equals(owningTutor.getTutorSeq()));
        if (!authorized) {
            return null;
        }

        return buildResponse(regradeRequest);
    }

    private RegradeRequestResponse buildResponse(RegradeRequest regradeRequest) {
        PaperSubmission paperSubmission = regradeRequest.getPaperSubmission();
        UplaodPaper paper = paperSubmission.getUplaodpaper();
        Classes classes = paper.getClasses();
        Student student = paperSubmission.getStudent();
        GradeSubmission gradeSubmission = regradeRequest.getGradeSubmission();

        RegradeRequestResponse response = new RegradeRequestResponse();
        response.setId(regradeRequest.getRegradeRequestSeq());
        response.setSubmission_id(paperSubmission.getPaperSubmissionSeq());
        response.setPaper_id(paper.getUploadPaperSeq());
        response.setPaper_name(paper.getPaperName());
        response.setClass_id(classes.getClassSeq());
        response.setClass_name(classes.getDisplayName());
        response.setStudent_name(student.getFirstName() + " " + student.getLastName());
        response.setStudent_number(student.getStudentNo());
        response.setReason(regradeRequest.getReason());
        response.setRequested_at(regradeRequest.getRequestedAt());
        response.setStatus(regradeRequest.getStatus() == 2 ? "COMPLETED" : "PENDING");
        response.setResolved_at(regradeRequest.getResolvedAt());

        List<QuestionGradeResponse> previousQuestions = new ArrayList<>();
        if (gradeSubmission != null) {
            response.setPrevious_total_marks(gradeSubmission.getTotalMarks());
            response.setPrevious_max_marks(gradeSubmission.getMaxMarks());
            response.setPrevious_grade(gradeSubmission.getGrade());

            List<GradeSubmissionQuestion> gradeSubmissionQuestions = gradeSubmissionQuestionRepository
                    .findByGradeSubmissionAndStatus(gradeSubmission, 2);
            for (GradeSubmissionQuestion gradedQuestion : gradeSubmissionQuestions) {
                QuestionGradeResponse questionResponse = new QuestionGradeResponse();
                questionResponse.setMarks_awarded(gradedQuestion.getMarksAwarded());
                questionResponse.setComment(gradedQuestion.getComment());

                boolean isSubQuestion = Boolean.TRUE.equals(gradedQuestion.getIsSubQuestion())
                        && gradedQuestion.getUploadPaperQuestionSubQuestion() != null;
                if (isSubQuestion) {
                    questionResponse.setQuestion_id(gradedQuestion.getUploadPaperQuestion().getUploadPaperQuestionSeq().toString()
                            + "-" + gradedQuestion.getUploadPaperQuestionSubQuestion().getUploadPaperQuestionSubQuestionSeq().toString());
                    questionResponse.setMax_marks(gradedQuestion.getUploadPaperQuestionSubQuestion().getMark());
                } else {
                    questionResponse.setQuestion_id(gradedQuestion.getUploadPaperQuestion().getUploadPaperQuestionSeq().toString());
                    questionResponse.setMax_marks(gradedQuestion.getUploadPaperQuestion().getMarks());
                }

                previousQuestions.add(questionResponse);
            }
        }
        response.setPrevious_questions(previousQuestions);

        return response;
    }
}
