package com.smartjobportal.mapper;

import com.smartjobportal.dto.resume.JobRecommendationResponse;
import com.smartjobportal.entity.JobRecommendation;
import org.springframework.stereotype.Component;

@Component
public class JobRecommendationMapper {

    public JobRecommendationResponse toResponse(JobRecommendation rec) {
        if (rec == null) {
            return null;
        }

        return JobRecommendationResponse.builder()
                .id(rec.getId())
                .jobId(rec.getJob() != null ? rec.getJob().getId() : null)
                .jobTitle(rec.getJob() != null ? rec.getJob().getTitle() : null)
                .companyName(rec.getJob() != null && rec.getJob().getCompany() != null ? rec.getJob().getCompany().getCompanyName() : null)
                .companyLogo(rec.getJob() != null && rec.getJob().getCompany() != null ? rec.getJob().getCompany().getLogoPath() : null)
                .location(rec.getJob() != null ? rec.getJob().getLocation() : null)
                .employmentType(rec.getJob() != null && rec.getJob().getEmploymentType() != null ? rec.getJob().getEmploymentType().name() : null)
                .minSalary(rec.getJob() != null ? rec.getJob().getMinSalary() : null)
                .maxSalary(rec.getJob() != null ? rec.getJob().getMaxSalary() : null)
                .matchScore(rec.getMatchScore())
                .reason(rec.getReason())
                .isViewed(rec.getIsViewed())
                .createdAt(rec.getCreatedAt())
                .build();
    }
}
