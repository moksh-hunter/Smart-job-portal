package com.smartjobportal.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecruiterDashboardResponse {
    private Long totalJobs;
    private Long openJobs;
    private Long closedJobs;
    private Long totalApplicants;
    private Long shortlistedApplicants;
    private Long selectedApplicants;
    private Long rejectedApplicants;
    private Long totalInterviews;
    private Long scheduledInterviews;
    private Long completedInterviews;
    private Double averageAtsScore;
    private Long totalCompanies;
}
