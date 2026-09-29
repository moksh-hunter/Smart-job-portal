package com.smartjobportal.dto.application;

import com.smartjobportal.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private ApplicationStatus status;

    private String recruiterNotes;
}
