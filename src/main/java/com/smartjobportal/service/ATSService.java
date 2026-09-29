package com.smartjobportal.service;

import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.dto.resume.ResumeScoreResponse;
import com.smartjobportal.entity.Application;
import com.smartjobportal.entity.CandidateProfile;
import com.smartjobportal.entity.Job;
import com.smartjobportal.entity.ResumeScore;
import com.smartjobportal.entity.User;
import com.smartjobportal.exception.ResourceNotFoundException;
import com.smartjobportal.mapper.ResumeScoreMapper;
import com.smartjobportal.repository.ApplicationRepository;
import com.smartjobportal.repository.CandidateProfileRepository;
import com.smartjobportal.repository.JobRepository;
import com.smartjobportal.repository.ResumeScoreRepository;
import com.smartjobportal.repository.UserRepository;
import com.smartjobportal.util.ATSScoreCalculator;
import com.smartjobportal.util.SkillMatchingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for handling ATS score calculations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ATSService {

    private final ResumeScoreRepository resumeScoreRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ResumeScoreMapper resumeScoreMapper;

    /**
     * Calculate ATS score for a candidate and job.
     */
    @Transactional
    public ApiResponse<ResumeScoreResponse> calculateATSScore(Long candidateId, Long jobId) {
        log.info("Calculating ATS score for candidateId: {}, jobId: {}", candidateId, jobId);

        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", candidateId));

        CandidateProfile profile = candidateProfileRepository.findByUserId(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("CandidateProfile", "userId", candidateId));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", jobId));

        double overallScore = ATSScoreCalculator.calculate(profile, job);
        double profileScore = ATSScoreCalculator.calculateProfileCompletenessScore(profile);
        double skillScore = ATSScoreCalculator.calculateSkillMatchScore(profile.getSkills(), job.getRequiredSkills());
        double expScore = ATSScoreCalculator.calculateExperienceScore(profile.getTotalExperience(), job.getMinExperience(), job.getMaxExperience());

        List<String> matchedSkills = SkillMatchingUtil.getMatchedSkills(profile.getSkills(), job.getRequiredSkills());
        List<String> missingSkills = SkillMatchingUtil.getMissingSkills(profile.getSkills(), job.getRequiredSkills());
        String improvementSuggestions = SkillMatchingUtil.generateImprovementSuggestions(missingSkills);

        Optional<ResumeScore> existingScore = resumeScoreRepository.findByCandidateIdAndJobId(candidateId, jobId);
        ResumeScore score = existingScore.orElseGet(ResumeScore::new);

        score.setCandidate(candidate);
        score.setJob(job);
        score.setOverallScore(overallScore);
        score.setProfileCompletenessScore(profileScore);
        score.setSkillMatchScore(skillScore);
        score.setExperienceScore(expScore);
        score.setMatchedSkills(matchedSkills);
        score.setMissingSkills(missingSkills);
        score.setImprovementSuggestions(improvementSuggestions);

        ResumeScore savedScore = resumeScoreRepository.save(score);

        return ApiResponse.success("ATS Score calculated successfully", resumeScoreMapper.toResponse(savedScore));
    }

    /**
     * Retrieve existing ATS score.
     */
    public ApiResponse<ResumeScoreResponse> getATSScore(Long candidateId, Long jobId) {
        ResumeScore score = resumeScoreRepository.findByCandidateIdAndJobId(candidateId, jobId)
                .orElseThrow(() -> new ResourceNotFoundException("ResumeScore", "candidateId and jobId", candidateId));
        return ApiResponse.success("ATS Score retrieved successfully", resumeScoreMapper.toResponse(score));
    }

    /**
     * Get all scores for a candidate.
     */
    public ApiResponse<List<ResumeScoreResponse>> getMyATSScores(Long candidateId) {
        List<ResumeScoreResponse> scores = resumeScoreRepository.findByCandidateId(candidateId).stream()
                .map(resumeScoreMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success("Scores retrieved successfully", scores);
    }

    /**
     * Get all scores for a job ordered by overallScore desc.
     */
    public ApiResponse<List<ResumeScoreResponse>> getATSScoresForJob(Long jobId) {
        List<ResumeScoreResponse> scores = resumeScoreRepository.findByJobIdOrderByOverallScoreDesc(jobId).stream()
                .map(resumeScoreMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success("Scores retrieved successfully", scores);
    }

    /**
     * Calculate and update ATS and ranking scores for all applications of a job.
     */
    @Transactional
    public ApiResponse<String> calculateAndUpdateApplicationScores(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", jobId));

        List<Application> applications = applicationRepository.findByJobId(jobId, org.springframework.data.domain.Pageable.unpaged()).getContent();

        for (Application app : applications) {
            CandidateProfile profile = candidateProfileRepository.findByUserId(app.getCandidate().getId())
                    .orElse(null);

            if (profile != null) {
                double atsScore = ATSScoreCalculator.calculate(profile, job);
                double rankingScore = ATSScoreCalculator.calculateRankingScore(atsScore, profile.getTotalExperience());
                app.setAtsScore(atsScore);
                app.setRankingScore(rankingScore);
            }
        }

        applicationRepository.saveAll(applications);

        return ApiResponse.success("Application scores calculated and updated successfully", null);
    }
}
