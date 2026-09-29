package com.smartjobportal.service;

import com.smartjobportal.dto.application.ApplicationRequest;
import com.smartjobportal.dto.application.ApplicationResponse;
import com.smartjobportal.dto.application.ApplicationStatusUpdateRequest;
import com.smartjobportal.dto.job.JobResponse;
import com.smartjobportal.dto.common.PagedResponse;
import com.smartjobportal.entity.Application;
import com.smartjobportal.entity.ApplicationStatus;
import com.smartjobportal.entity.Job;
import com.smartjobportal.entity.JobStatus;
import com.smartjobportal.entity.Notification;
import com.smartjobportal.entity.NotificationType;
import com.smartjobportal.entity.SavedJob;
import com.smartjobportal.entity.User;
import com.smartjobportal.exception.BadRequestException;
import com.smartjobportal.exception.ResourceNotFoundException;
import com.smartjobportal.mapper.ApplicationMapper;
import com.smartjobportal.mapper.JobMapper;
import com.smartjobportal.repository.ApplicationRepository;
import com.smartjobportal.repository.CandidateProfileRepository;
import com.smartjobportal.repository.JobRepository;
import com.smartjobportal.repository.NotificationRepository;
import com.smartjobportal.repository.SavedJobRepository;
import com.smartjobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final SavedJobRepository savedJobRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final ApplicationMapper applicationMapper;
    private final JobMapper jobMapper;

    @Transactional
    public ApplicationResponse applyForJob(Long userId, ApplicationRequest request) {
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", request.getJobId()));

        if (job.getStatus() != JobStatus.OPEN) {
            throw new BadRequestException("Job is not open for applications.");
        }

        if (applicationRepository.existsByCandidateIdAndJobId(userId, job.getId())) {
            throw new BadRequestException("You have already applied for this job.");
        }

        User candidate = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Application application = new Application();
        application.setCandidate(candidate);
        application.setJob(job);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setCoverLetter(request.getCoverLetter());

        candidateProfileRepository.findByUserId(userId).ifPresent(profile -> {
            application.setResumePath(profile.getResumePath());
        });

        Application savedApplication = applicationRepository.save(application);

        int currentApps = job.getApplicationsCount() != null ? job.getApplicationsCount() : 0;
        job.setApplicationsCount(currentApps + 1);
        jobRepository.save(job);

        Notification notification = Notification.builder()
                .user(job.getPostedBy())
                .type(NotificationType.JOB_APPLIED)
                .title("New Application Received")
                .message("New application from " + candidate.getFullName() + " for job " + job.getTitle())
                .isRead(false)
                .referenceId(savedApplication.getId())
                .referenceType("APPLICATION")
                .build();
        notificationRepository.save(notification);

        return applicationMapper.toResponse(savedApplication);
    }

    public PagedResponse<ApplicationResponse> getMyApplications(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        Page<Application> applications = applicationRepository.findByCandidateId(userId, pageable);

        List<ApplicationResponse> content = applications.getContent().stream()
                .map(applicationMapper::toResponse)
                .toList();

        return PagedResponse.<ApplicationResponse>builder()
                .content(content)
                .pageNumber(applications.getNumber())
                .pageSize(applications.getSize())
                .totalElements(applications.getTotalElements())
                .totalPages(applications.getTotalPages())
                .last(applications.isLast())
                .first(applications.isFirst())
                .build();
    }

    public PagedResponse<ApplicationResponse> getApplicationsForJob(Long recruiterId, Long jobId, int page, int size) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", jobId));

        if (!job.getPostedBy().getId().equals(recruiterId)) {
            throw new BadRequestException("You do not have permission to view applications for this job.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        Page<Application> applications = applicationRepository.findByJobId(jobId, pageable);

        List<ApplicationResponse> content = applications.getContent().stream()
                .map(applicationMapper::toResponse)
                .toList();

        return PagedResponse.<ApplicationResponse>builder()
                .content(content)
                .pageNumber(applications.getNumber())
                .pageSize(applications.getSize())
                .totalElements(applications.getTotalElements())
                .totalPages(applications.getTotalPages())
                .last(applications.isLast())
                .first(applications.isFirst())
                .build();
    }

    public ApplicationResponse getApplicationById(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId));
        return applicationMapper.toResponse(application);
    }

    @Transactional
    public ApplicationResponse updateApplicationStatus(Long recruiterId, Long applicationId, ApplicationStatusUpdateRequest request) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId));

        if (!application.getJob().getPostedBy().getId().equals(recruiterId)) {
            throw new BadRequestException("You do not have permission to update this application.");
        }

        application.setStatus(request.getStatus());
        application.setRecruiterNotes(request.getRecruiterNotes());

        Application updatedApplication = applicationRepository.save(application);

        emailService.sendApplicationStatusEmail(
                application.getCandidate().getEmail(),
                application.getCandidate().getFullName(),
                application.getJob().getTitle(),
                request.getStatus().name()
        );

        Notification notification = Notification.builder()
                .user(application.getCandidate())
                .type(NotificationType.APPLICATION_STATUS_CHANGED)
                .title("Application Status Updated")
                .message("Your application for " + application.getJob().getTitle() + " has been updated to " + request.getStatus())
                .isRead(false)
                .referenceId(application.getId())
                .referenceType("APPLICATION")
                .build();
        notificationRepository.save(notification);

        return applicationMapper.toResponse(updatedApplication);
    }

    @Transactional
    public ApplicationResponse withdrawApplication(Long userId, Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId));

        if (!application.getCandidate().getId().equals(userId)) {
            throw new BadRequestException("You do not have permission to withdraw this application.");
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);

        Application updatedApplication = applicationRepository.save(application);
        return applicationMapper.toResponse(updatedApplication);
    }

    @Transactional
    public void saveJob(Long userId, Long jobId) {
        if (savedJobRepository.existsByUserIdAndJobId(userId, jobId)) {
            throw new BadRequestException("Job already saved.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", jobId));

        SavedJob savedJob = SavedJob.builder()
                .user(user)
                .job(job)
                .build();

        savedJobRepository.save(savedJob);
    }

    @Transactional
    public void unsaveJob(Long userId, Long jobId) {
        if (!savedJobRepository.existsByUserIdAndJobId(userId, jobId)) {
            throw new BadRequestException("Job is not saved.");
        }
        savedJobRepository.deleteByUserIdAndJobId(userId, jobId);
    }

    public PagedResponse<JobResponse> getSavedJobs(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("savedAt").descending());
        Page<SavedJob> savedJobs = savedJobRepository.findByUserId(userId, pageable);

        List<JobResponse> content = savedJobs.getContent().stream()
                .map(savedJob -> jobMapper.toResponse(savedJob.getJob()))
                .toList();

        return PagedResponse.<JobResponse>builder()
                .content(content)
                .pageNumber(savedJobs.getNumber())
                .pageSize(savedJobs.getSize())
                .totalElements(savedJobs.getTotalElements())
                .totalPages(savedJobs.getTotalPages())
                .last(savedJobs.isLast())
                .first(savedJobs.isFirst())
                .build();
    }

    public PagedResponse<ApplicationResponse> getRankedApplicants(Long recruiterId, Long jobId, int page, int size) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", jobId));

        if (!job.getPostedBy().getId().equals(recruiterId)) {
            throw new BadRequestException("You do not have permission to view ranked applicants for this job.");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Application> applications = applicationRepository.findByJobIdOrderByRankingScoreDesc(jobId, pageable);

        List<ApplicationResponse> content = applications.getContent().stream()
                .map(applicationMapper::toResponse)
                .toList();

        return PagedResponse.<ApplicationResponse>builder()
                .content(content)
                .pageNumber(applications.getNumber())
                .pageSize(applications.getSize())
                .totalElements(applications.getTotalElements())
                .totalPages(applications.getTotalPages())
                .last(applications.isLast())
                .first(applications.isFirst())
                .build();
    }
}
