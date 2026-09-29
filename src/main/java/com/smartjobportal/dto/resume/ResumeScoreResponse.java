package com.smartjobportal.dto.resume;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResumeScoreResponse {
    private Long id;
    private Long candidateId;
    private String candidateName;
    private Long jobId;
    private String jobTitle;
    private Double overallScore;
    private Double profileCompletenessScore;
    private Double skillMatchScore;
    private Double experienceScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private String improvementSuggestions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
