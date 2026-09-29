package com.smartjobportal.mapper;

import com.smartjobportal.dto.application.ApplicationResponse;
import com.smartjobportal.entity.Application;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public ApplicationResponse toResponse(Application application) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .candidateId(application.getCandidate().getId())
                .candidateName(application.getCandidate().getFullName())
                .candidateEmail(application.getCandidate().getEmail())
                .jobId(application.getJob().getId())
                .jobTitle(application.getJob().getTitle())
                .companyName(application.getJob().getCompany().getCompanyName())
                .status(application.getStatus())
                .coverLetter(application.getCoverLetter())
                .resumePath(application.getResumePath())
                .recruiterNotes(application.getRecruiterNotes())
                .rankingScore(application.getRankingScore())
                .atsScore(application.getAtsScore())
                .appliedAt(application.getAppliedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}
