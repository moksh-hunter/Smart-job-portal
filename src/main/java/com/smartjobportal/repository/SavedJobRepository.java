package com.smartjobportal.repository;

import com.smartjobportal.entity.SavedJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    Page<SavedJob> findByUserId(Long userId, Pageable pageable);
    Optional<SavedJob> findByUserIdAndJobId(Long userId, Long jobId);
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
    void deleteByUserIdAndJobId(Long userId, Long jobId);
}
