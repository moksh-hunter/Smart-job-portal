package com.smartjobportal.repository;

import com.smartjobportal.entity.Job;
import com.smartjobportal.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    Page<Job> findByStatus(JobStatus status, Pageable pageable);
    Page<Job> findByCompanyId(Long companyId, Pageable pageable);
    List<Job> findByPostedById(Long recruiterId);
    Page<Job> findByPostedById(Long recruiterId, Pageable pageable);
    Page<Job> findByPostedByIdAndStatus(Long recruiterId, JobStatus status, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = 'OPEN' AND j.location LIKE %:location%")
    Page<Job> findOpenJobsByLocation(@Param("location") String location, Pageable pageable);

    @Query("SELECT j FROM Job j JOIN j.requiredSkills s WHERE j.status = 'OPEN' AND LOWER(s) IN :skills")
    List<Job> findOpenJobsBySkills(@Param("skills") List<String> skills);

    @Query("SELECT COUNT(j) FROM Job j WHERE j.status = 'OPEN'")
    long countOpenJobs();

    @Query("SELECT COUNT(j) FROM Job j WHERE j.postedBy.id = :recruiterId")
    long countByRecruiterId(@Param("recruiterId") Long recruiterId);

    @Query("SELECT COUNT(j) FROM Job j")
    long countTotalJobs();
}
