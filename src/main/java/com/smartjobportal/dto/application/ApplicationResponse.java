package com.smartjobportal.dto.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartjobportal.entity.ApplicationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationResponse {
    private Long id;
    private Long candidateId;
    private String candidateName;
    private String candidateEmail;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private ApplicationStatus status;
    private String coverLetter;
    private String resumePath;
    private String recruiterNotes;
    private Double rankingScore;
    private Double atsScore;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
