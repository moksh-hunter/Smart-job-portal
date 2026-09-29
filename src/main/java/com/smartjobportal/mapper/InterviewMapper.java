package com.smartjobportal.mapper;

import com.smartjobportal.dto.interview.InterviewRequest;
import com.smartjobportal.dto.interview.InterviewResponse;
import com.smartjobportal.entity.Application;
import com.smartjobportal.entity.Interview;
import org.springframework.stereotype.Component;

@Component
public class InterviewMapper {

    public InterviewResponse toResponse(Interview interview) {
        Application application = interview.getApplication();
        return InterviewResponse.builder()
                .id(interview.getId())
                .applicationId(application.getId())
                .candidateId(application.getCandidate().getId())
                .candidateName(application.getCandidate().getFullName())
                .candidateEmail(application.getCandidate().getEmail())
                .jobId(application.getJob().getId())
                .jobTitle(application.getJob().getTitle())
                .companyName(application.getJob().getCompany().getCompanyName())
                .interviewDate(interview.getInterviewDate())
                .interviewTime(interview.getInterviewTime())
                .round(interview.getRound())
                .interviewerName(interview.getInterviewerName())
                .interviewerEmail(interview.getInterviewerEmail())
                .meetingLink(interview.getMeetingLink())
                .location(interview.getLocation())
                .status(interview.getStatus())
                .feedback(interview.getFeedback())
                .rating(interview.getRating())
                .notes(interview.getNotes())
                .createdAt(interview.getCreatedAt())
                .updatedAt(interview.getUpdatedAt())
                .build();
    }

    public Interview toEntity(InterviewRequest request, Application application) {
        return Interview.builder()
                .application(application)
                .interviewDate(request.getInterviewDate())
                .interviewTime(request.getInterviewTime())
                .round(request.getRound())
                .interviewerName(request.getInterviewerName())
                .interviewerEmail(request.getInterviewerEmail())
                .meetingLink(request.getMeetingLink())
                .location(request.getLocation())
                .notes(request.getNotes())
                .build();
    }

    public void updateEntity(Interview interview, InterviewRequest request) {
        if (request.getInterviewDate() != null) interview.setInterviewDate(request.getInterviewDate());
        if (request.getInterviewTime() != null) interview.setInterviewTime(request.getInterviewTime());
        if (request.getRound() != null) interview.setRound(request.getRound());
        if (request.getInterviewerName() != null) interview.setInterviewerName(request.getInterviewerName());
        if (request.getInterviewerEmail() != null) interview.setInterviewerEmail(request.getInterviewerEmail());
        if (request.getMeetingLink() != null) interview.setMeetingLink(request.getMeetingLink());
        if (request.getLocation() != null) interview.setLocation(request.getLocation());
        if (request.getNotes() != null) interview.setNotes(request.getNotes());
    }
}
