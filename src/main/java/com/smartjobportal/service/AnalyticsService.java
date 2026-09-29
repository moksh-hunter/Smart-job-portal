package com.smartjobportal.service;

import com.smartjobportal.dto.admin.DashboardStatsResponse;
import com.smartjobportal.dto.admin.RecruiterDashboardResponse;
import com.smartjobportal.entity.ApplicationStatus;
import com.smartjobportal.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getAdminDashboard() {
        log.info("Generating admin dashboard statistics");
        DashboardStatsResponse stats = new DashboardStatsResponse();
        
        stats.setTotalUsers(userRepository.count());
        stats.setActiveUsers(userRepository.countActiveUsers());
        stats.setTotalCandidates(userRepository.countCandidates());
        stats.setTotalRecruiters(userRepository.countRecruiters());
        stats.setActiveRecruiters(userRepository.countRecruiters()); 
        
        stats.setTotalJobs(jobRepository.countTotalJobs());
        stats.setOpenJobs(jobRepository.countOpenJobs());
        stats.setClosedJobs(stats.getTotalJobs() - stats.getOpenJobs()); 
        
        stats.setTotalApplications(applicationRepository.countTotalApplications());
        stats.setPendingApplications(applicationRepository.countByStatus(ApplicationStatus.APPLIED));
        stats.setShortlistedApplications(applicationRepository.countByStatus(ApplicationStatus.SHORTLISTED));
        stats.setRejectedApplications(applicationRepository.countByStatus(ApplicationStatus.REJECTED));
        
        stats.setTotalInterviews(interviewRepository.count());
        stats.setScheduledInterviews(interviewRepository.count()); 
        
        // Placeholder for monthly trends
        stats.setMonthlyApplications(null);
        stats.setMonthlyJobPostings(null);
        stats.setMonthlyRegistrations(null);
        
        return stats;
    }

    @Transactional(readOnly = true)
    public RecruiterDashboardResponse getRecruiterDashboard(Long recruiterId) {
        log.info("Generating recruiter dashboard statistics for recruiter {}", recruiterId);
        RecruiterDashboardResponse stats = new RecruiterDashboardResponse();
        
        long totalJobs = jobRepository.countByRecruiterId(recruiterId);
        stats.setTotalJobs(totalJobs);
        stats.setOpenJobs(totalJobs); 
        stats.setClosedJobs(0L);
        
        long totalApplicants = applicationRepository.countByRecruiterId(recruiterId);
        stats.setTotalApplicants(totalApplicants);
        stats.setShortlistedApplicants(0L);
        stats.setSelectedApplicants(0L);
        stats.setRejectedApplicants(0L);
        
        long totalInterviews = interviewRepository.countByRecruiterId(recruiterId);
        stats.setTotalInterviews(totalInterviews);
        stats.setScheduledInterviews(totalInterviews);
        stats.setCompletedInterviews(0L);
        
        stats.setAverageAtsScore(0.0);
        
        stats.setTotalCompanies((long) companyRepository.findByRecruiterId(recruiterId).size());
        
        return stats;
    }
}
