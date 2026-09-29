package com.smartjobportal.service;

import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.dto.job.JobRequest;
import com.smartjobportal.dto.job.JobResponse;
import com.smartjobportal.dto.common.PagedResponse;
import com.smartjobportal.entity.Company;
import com.smartjobportal.entity.Job;
import com.smartjobportal.entity.User;
import com.smartjobportal.entity.EmploymentType;
import com.smartjobportal.entity.JobStatus;
import com.smartjobportal.exception.BadRequestException;
import com.smartjobportal.exception.ResourceNotFoundException;
import com.smartjobportal.mapper.JobMapper;
import com.smartjobportal.repository.CompanyRepository;
import com.smartjobportal.repository.JobRepository;
import com.smartjobportal.repository.UserRepository;
import com.smartjobportal.specification.JobSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final JobMapper jobMapper;

    @Transactional
    public ApiResponse<JobResponse> createJob(Long userId, JobRequest request) {
        log.info("Creating new job for company ID: {} by user ID: {}", request.getCompanyId(), userId);
        
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", request.getCompanyId()));
                
        if (!company.getRecruiter().getId().equals(userId)) {
            throw new BadRequestException("You don't have permission to post jobs for this company");
        }
        
        User recruiter = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Job job = jobMapper.toEntity(request);
        job.setCompany(company);
        job.setPostedBy(recruiter);
        job.setStatus(JobStatus.OPEN);
        job.setViewsCount(0);
        job.setApplicationsCount(0);

        Job savedJob = jobRepository.save(job);
        return ApiResponse.success("Job created successfully", jobMapper.toResponse(savedJob));
    }

    @Transactional
    public ApiResponse<JobResponse> updateJob(Long userId, Long jobId, JobRequest request) {
        log.info("Updating job ID: {} by user ID: {}", jobId, userId);
        
        Job job = getJobAndVerifyOwner(userId, jobId);
        
        if (request.getCompanyId() != null && !job.getCompany().getId().equals(request.getCompanyId())) {
             Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", request.getCompanyId()));
                
            if (!company.getRecruiter().getId().equals(userId)) {
                throw new BadRequestException("You don't have permission to assign jobs to this company");
            }
            job.setCompany(company);
        }

        jobMapper.updateEntity(job, request);
        Job updatedJob = jobRepository.save(job);
        
        return ApiResponse.success("Job updated successfully", jobMapper.toResponse(updatedJob));
    }

    @Transactional
    public ApiResponse<JobResponse> getJobById(Long jobId) {
        log.info("Fetching job ID: {}", jobId);
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", jobId));
                
        job.setViewsCount(job.getViewsCount() + 1);
        jobRepository.save(job);
        
        return ApiResponse.success("Job fetched successfully", jobMapper.toResponse(job));
    }

    @Transactional(readOnly = true)
    public ApiResponse<PagedResponse<JobResponse>> searchJobs(
            String keyword, String location, Long companyId, EmploymentType employmentType,
            Double minSalary, Double maxSalary, Double experience, Boolean isRemote, 
            List<String> skills, int page, int size, String sortBy, String sortDir) {
            
        log.info("Searching jobs with keyword: {}, location: {}", keyword, location);
        
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? 
                    Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Specification<Job> spec = Specification.where(JobSpecification.byStatus(JobStatus.OPEN))
                .and(JobSpecification.byTitle(keyword))
                .and(JobSpecification.byLocation(location))
                .and(JobSpecification.byCompanyId(companyId))
                .and(JobSpecification.byEmploymentType(employmentType))
                .and(JobSpecification.byMinSalary(minSalary))
                .and(JobSpecification.byMaxSalary(maxSalary))
                .and(JobSpecification.byExperience(experience))
                .and(JobSpecification.byIsRemote(isRemote))
                .and(JobSpecification.bySkills(skills));
                
        Page<Job> jobPage = jobRepository.findAll(spec, pageable);
        
        List<JobResponse> content = jobPage.getContent().stream()
                .map(jobMapper::toResponse)
                .collect(Collectors.toList());
                
        PagedResponse<JobResponse> pagedResponse = new PagedResponse<>(
                content, jobPage.getNumber(), jobPage.getSize(), 
                jobPage.getTotalElements(), jobPage.getTotalPages(), 
                jobPage.isLast(), jobPage.isFirst());
                
        return ApiResponse.success("Jobs retrieved successfully", pagedResponse);
    }

    @Transactional(readOnly = true)
    public ApiResponse<PagedResponse<JobResponse>> getJobsByRecruiter(
            Long userId, JobStatus status, int page, int size) {
            
        log.info("Fetching jobs for recruiter ID: {}", userId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Job> jobPage;
        
        if (status != null) {
            jobPage = jobRepository.findByPostedByIdAndStatus(userId, status, pageable);
        } else {
            jobPage = jobRepository.findByPostedById(userId, pageable);
        }
        
        List<JobResponse> content = jobPage.getContent().stream()
                .map(jobMapper::toResponse)
                .collect(Collectors.toList());
                
        PagedResponse<JobResponse> pagedResponse = new PagedResponse<>(
                content, jobPage.getNumber(), jobPage.getSize(), 
                jobPage.getTotalElements(), jobPage.getTotalPages(), 
                jobPage.isLast(), jobPage.isFirst());
                
        return ApiResponse.success("Jobs fetched successfully", pagedResponse);
    }

    @Transactional
    public ApiResponse<Void> closeJob(Long userId, Long jobId) {
        log.info("Closing job ID: {} by user ID: {}", jobId, userId);
        Job job = getJobAndVerifyOwner(userId, jobId);
        job.setStatus(JobStatus.CLOSED);
        jobRepository.save(job);
        return ApiResponse.success("Job closed successfully");
    }

    @Transactional
    public ApiResponse<Void> reopenJob(Long userId, Long jobId) {
        log.info("Reopening job ID: {} by user ID: {}", jobId, userId);
        Job job = getJobAndVerifyOwner(userId, jobId);
        job.setStatus(JobStatus.OPEN);
        jobRepository.save(job);
        return ApiResponse.success("Job reopened successfully");
    }

    @Transactional
    public ApiResponse<Void> deleteJob(Long userId, Long jobId) {
        log.info("Deleting job ID: {} by user ID: {}", jobId, userId);
        Job job = getJobAndVerifyOwner(userId, jobId);
        jobRepository.delete(job);
        return ApiResponse.success("Job deleted successfully");
    }
    
    private Job getJobAndVerifyOwner(Long userId, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", jobId));
                
        if (!job.getPostedBy().getId().equals(userId)) {
            throw new BadRequestException("You don't have permission to modify this job");
        }
        return job;
    }
}
