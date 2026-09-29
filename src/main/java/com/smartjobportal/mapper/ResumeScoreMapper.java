package com.smartjobportal.mapper;

import com.smartjobportal.dto.resume.ResumeScoreResponse;
import com.smartjobportal.entity.ResumeScore;
import org.springframework.stereotype.Component;

@Component
public class ResumeScoreMapper {

    public ResumeScoreResponse toResponse(ResumeScore score) {
        return ResumeScoreResponse.builder()
                .id(score.getId())
                .candidateId(score.getCandidate().getId())
                .candidateName(score.getCandidate().getFullName())
                .jobId(score.getJob() != null ? score.getJob().getId() : null)
                .jobTitle(score.getJob() != null ? score.getJob().getTitle() : null)
                .overallScore(score.getOverallScore())
                .profileCompletenessScore(score.getProfileCompletenessScore())
                .skillMatchScore(score.getSkillMatchScore())
                .experienceScore(score.getExperienceScore())
                .matchedSkills(score.getMatchedSkills())
                .missingSkills(score.getMissingSkills())
                .improvementSuggestions(score.getImprovementSuggestions())
                .createdAt(score.getCreatedAt())
                .updatedAt(score.getUpdatedAt())
                .build();
    }
}
