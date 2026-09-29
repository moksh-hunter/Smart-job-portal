package com.smartjobportal.repository;

import com.smartjobportal.entity.ResumeScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeScoreRepository extends JpaRepository<ResumeScore, Long> {
    List<ResumeScore> findByCandidateId(Long candidateId);
    Optional<ResumeScore> findByCandidateIdAndJobId(Long candidateId, Long jobId);
    List<ResumeScore> findByJobIdOrderByOverallScoreDesc(Long jobId);
}
