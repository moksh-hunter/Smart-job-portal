package com.smartjobportal.dto.interview;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRequest {

    @NotNull(message = "Application ID is required")
    private Long applicationId;

    @NotNull(message = "Interview date is required")
    @Future(message = "Interview date must be in the future")
    private LocalDate interviewDate;

    @NotNull(message = "Interview time is required")
    private LocalTime interviewTime;

    @NotBlank(message = "Round is required")
    @Size(max = 100)
    private String round;

    @Size(max = 200)
    private String interviewerName;

    @Size(max = 200)
    private String interviewerEmail;

    @Size(max = 500)
    private String meetingLink;

    @Size(max = 300)
    private String location;

    private String notes;
}
