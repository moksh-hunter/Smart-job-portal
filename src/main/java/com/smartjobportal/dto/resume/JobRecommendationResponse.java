package com.smartjobportal.dto.resume;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobRecommendationResponse {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String companyLogo;
    private String location;
    private String employmentType;
    private Double minSalary;
    private Double maxSalary;
    private Double matchScore;
    private String reason;
    private Boolean isViewed;
    private LocalDateTime createdAt;
}
