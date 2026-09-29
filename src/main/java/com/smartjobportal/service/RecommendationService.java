package com.smartjobportal.service;

import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.dto.common.PagedResponse;
import com.smartjobportal.dto.resume.JobRecommendationResponse;
import com.smartjobportal.entity.Application;
import com.smartjobportal.entity.CandidateProfile;
import com.smartjobportal.entity.Job;
import com.smartjobportal.entity.JobRecommendation;
import com.smartjobportal.entity.User;
import com.smartjobportal.exception.ResourceNotFoundException;
import com.smartjobportal.mapper.JobRecommendationMapper;
import com.smartjobportal.repository.ApplicationRepository;
import com.smartjobportal.repository.CandidateProfileRepository;
import com.smartjobportal.repository.JobRecommendationRepository;
import com.smartjobportal.repository.JobRepository;
import com.smartjobportal.repository.UserRepository;
import com.smartjobportal.util.ATSScoreCalculator;
import com.smartjobportal.util.SkillMatchingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for recommendations and skill gap analysis.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final JobRecommendationRepository recommendationRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final JobRecommendationMapper jobRecommendationMapper;

    /**
     * Generate job recommendations for a candidate.
     */
    @Transactional
    public ApiResponse<List<JobRecommendationResponse>> generateRecommendations(Long candidateId) {
        log.info("Generating recommendations for candidateId: {}", candidateId);

        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", candidateId));

        CandidateProfile profile = candidateProfileRepository.findByUserId(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("CandidateProfile", "userId", candidateId));

        recommendationRepository.deleteByCandidateId(candidateId);

        List<Job> allJobs = jobRepository.findAll();
        List<JobRecommendation> newRecommendations = new ArrayList<>();

        for (Job job : allJobs) {
            double matchScore = SkillMatchingUtil.calculateMatchPercentage(profile.getSkills(), job.getRequiredSkills());
            if (matchScore > 30.0) {
                JobRecommendation rec = new JobRecommendation();
                rec.setCandidate(candidate);
                rec.setJob(job);
                rec.setMatchScore(matchScore);
                rec.setReason("Good match based on your skills");
                rec.setIsViewed(false);
                newRecommendations.add(rec);
            }
        }

        List<JobRecommendation> savedRecs = recommendationRepository.saveAll(newRecommendations);
        List<JobRecommendationResponse> responses = savedRecs.stream()
                .map(jobRecommendationMapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.success("Recommendations generated successfully", responses);
    }

    /**
     * Get paginated recommendations for a candidate.
     */
    public ApiResponse<PagedResponse<JobRecommendationResponse>> getRecommendations(Long candidateId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobRecommendation> recPage = recommendationRepository.findByCandidateIdOrderByMatchScoreDesc(candidateId, pageable);

        List<JobRecommendationResponse> responses = recPage.getContent().stream()
                .map(jobRecommendationMapper::toResponse)
                .collect(Collectors.toList());

        PagedResponse<JobRecommendationResponse> pagedResponse = PagedResponse.<JobRecommendationResponse>builder()
                .content(responses)
                .pageNumber(recPage.getNumber())
                .pageSize(recPage.getSize())
                .totalElements(recPage.getTotalElements())
                .totalPages(recPage.getTotalPages())
                .last(recPage.isLast())
                .first(recPage.isFirst())
                .build();

        return ApiResponse.success("Recommendations retrieved successfully", pagedResponse);
    }

    /**
     * Analyze skill gap between a candidate and a job.
     */
    public ApiResponse<Map<String, Object>> analyzeSkillGap(Long candidateId, Long jobId) {
        CandidateProfile profile = candidateProfileRepository.findByUserId(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("CandidateProfile", "userId", candidateId));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", jobId));

        List<String> matchedSkills = SkillMatchingUtil.getMatchedSkills(profile.getSkills(), job.getRequiredSkills());
        List<String> missingSkills = SkillMatchingUtil.getMissingSkills(profile.getSkills(), job.getRequiredSkills());
        double matchPercentage = SkillMatchingUtil.calculateMatchPercentage(profile.getSkills(), job.getRequiredSkills());
        String suggestions = SkillMatchingUtil.generateImprovementSuggestions(missingSkills);

        Map<String, Object> result = new HashMap<>();
        result.put("matchedSkills", matchedSkills);
        result.put("missingSkills", missingSkills);
        result.put("matchPercentage", matchPercentage);
        result.put("improvementSuggestions", suggestions);

        return ApiResponse.success("Skill gap analyzed successfully", result);
    }

    /**
     * Get ranked candidates for a job based on applications.
     */
    @Transactional
    public ApiResponse<List<Application>> getRankedCandidates(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", jobId));

        List<Application> applications = applicationRepository.findByJobId(jobId, Pageable.unpaged()).getContent();

        for (Application app : applications) {
            CandidateProfile profile = candidateProfileRepository.findByUserId(app.getCandidate().getId())
                    .orElse(null);
            if (profile != null) {
                double atsScore = ATSScoreCalculator.calculate(profile, job);
                app.setAtsScore(atsScore);
                app.setRankingScore(ATSScoreCalculator.calculateRankingScore(atsScore, profile.getTotalExperience()));
            }
        }

        applicationRepository.saveAll(applications);

        Page<Application> rankedPage = applicationRepository.findByJobIdOrderByRankingScoreDesc(jobId, Pageable.unpaged());

        return ApiResponse.success("Ranked candidates retrieved successfully", rankedPage.getContent());
    }
}
