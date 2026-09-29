package com.smartjobportal.repository;

import com.smartjobportal.entity.Interview;
import com.smartjobportal.entity.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByApplicationId(Long applicationId);
    List<Interview> findByStatus(InterviewStatus status);

    @Query("SELECT i FROM Interview i WHERE i.application.candidate.id = :candidateId ORDER BY i.interviewDate DESC")
    List<Interview> findByCandidateId(@Param("candidateId") Long candidateId);

    @Query("SELECT COUNT(i) FROM Interview i WHERE i.application.job.postedBy.id = :recruiterId")
    long countByRecruiterId(@Param("recruiterId") Long recruiterId);
}
