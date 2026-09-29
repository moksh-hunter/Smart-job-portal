package com.smartjobportal.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardStatsResponse {
    // User stats
    private Long totalUsers;
    private Long totalCandidates;
    private Long totalRecruiters;
    private Long activeUsers;

    // Job stats
    private Long totalJobs;
    private Long openJobs;
    private Long closedJobs;
    private Long activeRecruiters;

    // Application stats
    private Long totalApplications;
    private Long pendingApplications;
    private Long shortlistedApplications;
    private Long rejectedApplications;

    // Interview stats
    private Long totalInterviews;
    private Long scheduledInterviews;

    // Monthly trend (month -> count)
    private Map<String, Long> monthlyApplications;
    private Map<String, Long> monthlyJobPostings;
    private Map<String, Long> monthlyRegistrations;
}
