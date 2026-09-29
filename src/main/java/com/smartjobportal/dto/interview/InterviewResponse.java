package com.smartjobportal.dto.interview;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartjobportal.entity.InterviewStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InterviewResponse {
    private Long id;
    private Long applicationId;
    private Long candidateId;
    private String candidateName;
    private String candidateEmail;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private LocalDate interviewDate;
    private LocalTime interviewTime;
    private String round;
    private String interviewerName;
    private String interviewerEmail;
    private String meetingLink;
    private String location;
    private InterviewStatus status;
    private String feedback;
    private Integer rating;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
