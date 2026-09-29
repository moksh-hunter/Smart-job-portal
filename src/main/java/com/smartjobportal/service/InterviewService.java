package com.smartjobportal.service;

import com.smartjobportal.entity.Application;
import com.smartjobportal.entity.Interview;
import com.smartjobportal.entity.Notification;
import com.smartjobportal.entity.ApplicationStatus;
import com.smartjobportal.entity.InterviewStatus;
import com.smartjobportal.entity.NotificationType;
import com.smartjobportal.dto.interview.InterviewFeedbackRequest;
import com.smartjobportal.dto.interview.InterviewRequest;
import com.smartjobportal.dto.interview.InterviewResponse;
import com.smartjobportal.exception.BadRequestException;
import com.smartjobportal.exception.ResourceNotFoundException;
import com.smartjobportal.mapper.InterviewMapper;
import com.smartjobportal.repository.ApplicationRepository;
import com.smartjobportal.repository.InterviewRepository;
import com.smartjobportal.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final InterviewMapper interviewMapper;

    @Transactional
    public InterviewResponse scheduleInterview(Long recruiterId, InterviewRequest request) {
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", request.getApplicationId()));

        if (!application.getJob().getPostedBy().getId().equals(recruiterId)) {
            throw new BadRequestException("You do not have permission to schedule an interview for this application.");
        }

        Interview interview = Interview.builder()
                .application(application)
                .interviewDate(request.getInterviewDate())
                .interviewTime(request.getInterviewTime())
                .round(request.getRound())
                .interviewerName(request.getInterviewerName())
                .interviewerEmail(request.getInterviewerEmail())
                .meetingLink(request.getMeetingLink())
                .location(request.getLocation())
                .status(InterviewStatus.SCHEDULED)
                .build();

        Interview savedInterview = interviewRepository.save(interview);

        application.setStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        applicationRepository.save(application);

        emailService.sendInterviewScheduledEmail(
                application.getCandidate().getEmail(),
                application.getCandidate().getFullName(),
                application.getJob().getTitle(),
                request.getInterviewDate().toString(),
                request.getInterviewTime().toString(),
                request.getRound()
        );

        Notification notification = Notification.builder()
                .user(application.getCandidate())
                .type(NotificationType.INTERVIEW_SCHEDULED)
                .title("Interview Scheduled")
                .message("An interview has been scheduled for your application to " + application.getJob().getTitle())
                .isRead(false)
                .referenceId(savedInterview.getId())
                .referenceType("INTERVIEW")
                .build();
        notificationRepository.save(notification);

        return interviewMapper.toResponse(savedInterview);
    }

    @Transactional
    public InterviewResponse updateInterview(Long recruiterId, Long interviewId, InterviewRequest request) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview", "id", interviewId));

        if (!interview.getApplication().getJob().getPostedBy().getId().equals(recruiterId)) {
            throw new BadRequestException("You do not have permission to update this interview.");
        }

        interview.setInterviewDate(request.getInterviewDate());
        interview.setInterviewTime(request.getInterviewTime());
        interview.setRound(request.getRound());
        interview.setInterviewerName(request.getInterviewerName());
        interview.setInterviewerEmail(request.getInterviewerEmail());
        interview.setMeetingLink(request.getMeetingLink());
        interview.setLocation(request.getLocation());

        Interview updatedInterview = interviewRepository.save(interview);
        return interviewMapper.toResponse(updatedInterview);
    }

    @Transactional
    public InterviewResponse cancelInterview(Long recruiterId, Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview", "id", interviewId));

        if (!interview.getApplication().getJob().getPostedBy().getId().equals(recruiterId)) {
            throw new BadRequestException("You do not have permission to cancel this interview.");
        }

        interview.setStatus(InterviewStatus.CANCELLED);
        Interview cancelledInterview = interviewRepository.save(interview);

        Notification notification = Notification.builder()
                .user(interview.getApplication().getCandidate())
                .type(NotificationType.INTERVIEW_CANCELLED)
                .title("Interview Cancelled")
                .message("Your interview for " + interview.getApplication().getJob().getTitle() + " has been cancelled.")
                .isRead(false)
                .referenceId(interview.getId())
                .referenceType("INTERVIEW")
                .build();
        notificationRepository.save(notification);

        return interviewMapper.toResponse(cancelledInterview);
    }

    @Transactional
    public InterviewResponse submitFeedback(Long recruiterId, Long interviewId, InterviewFeedbackRequest request) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview", "id", interviewId));

        if (!interview.getApplication().getJob().getPostedBy().getId().equals(recruiterId)) {
            throw new BadRequestException("You do not have permission to submit feedback for this interview.");
        }

        interview.setFeedback(request.getFeedback());
        interview.setRating(request.getRating());
        interview.setStatus(request.getStatus() != null ? request.getStatus() : InterviewStatus.COMPLETED);

        Interview updatedInterview = interviewRepository.save(interview);
        return interviewMapper.toResponse(updatedInterview);
    }

    public List<InterviewResponse> getInterviewsByApplication(Long applicationId) {
        List<Interview> interviews = interviewRepository.findByApplicationId(applicationId);
        return interviews.stream()
                .map(interviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<InterviewResponse> getMyInterviews(Long candidateId) {
        List<Interview> interviews = interviewRepository.findByCandidateId(candidateId);
        return interviews.stream()
                .map(interviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    public InterviewResponse getInterviewById(Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview", "id", interviewId));
        return interviewMapper.toResponse(interview);
    }
}
