package com.smartjobportal.repository;

import com.smartjobportal.entity.Application;
import com.smartjobportal.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findByCandidateId(Long candidateId, Pageable pageable);
    Page<Application> findByJobId(Long jobId, Pageable pageable);
    List<Application> findByJobIdAndStatus(Long jobId, ApplicationStatus status);
    Optional<Application> findByCandidateIdAndJobId(Long candidateId, Long jobId);
    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.postedBy.id = :recruiterId")
    long countByRecruiterId(@Param("recruiterId") Long recruiterId);

    @Query("SELECT COUNT(a) FROM Application a")
    long countTotalApplications();

    @Query("SELECT COUNT(a) FROM Application a WHERE a.status = :status")
    long countByStatus(@Param("status") ApplicationStatus status);

    @Query("SELECT a FROM Application a WHERE a.job.id = :jobId ORDER BY a.rankingScore DESC NULLS LAST")
    Page<Application> findByJobIdOrderByRankingScoreDesc(@Param("jobId") Long jobId, Pageable pageable);
}
