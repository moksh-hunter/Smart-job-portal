package com.smartjobportal.dto.interview;

import com.smartjobportal.entity.InterviewStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewFeedbackRequest {

    @NotNull(message = "Interview status is required")
    private InterviewStatus status;

    private String feedback;

    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;
}
