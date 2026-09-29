package com.smartjobportal.repository;

import com.smartjobportal.entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long>, JpaSpecificationExecutor<CandidateProfile> {
    Optional<CandidateProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);

    @Query("SELECT cp FROM CandidateProfile cp WHERE cp.isOpenToWork = true")
    List<CandidateProfile> findOpenToWorkCandidates();

    @Query("SELECT cp FROM CandidateProfile cp JOIN cp.skills s WHERE s IN :skills")
    List<CandidateProfile> findBySkillsIn(List<String> skills);
}
