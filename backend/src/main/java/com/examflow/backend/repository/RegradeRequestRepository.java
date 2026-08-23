package com.examflow.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examflow.backend.entity.PaperSubmission;
import com.examflow.backend.entity.RegradeRequest;
import com.examflow.backend.entity.Tutor;

public interface RegradeRequestRepository extends JpaRepository<RegradeRequest, Integer> {

    RegradeRequest findByPaperSubmissionAndStatus(PaperSubmission paperSubmission, Integer status);

    RegradeRequest findByRegradeRequestSeq(Integer regradeRequestSeq);

    RegradeRequest findTopByPaperSubmissionOrderByRequestedAtDesc(PaperSubmission paperSubmission);

    // Tutors is a list so this single query serves both a teacher (their one class-owning
    // tutor) and an instructor (every tutor they're engaged with).
    List<RegradeRequest> findByStatusAndPaperSubmission_Uplaodpaper_Classes_TutorInOrderByRequestedAtAsc(
            Integer status, List<Tutor> tutors);
}
