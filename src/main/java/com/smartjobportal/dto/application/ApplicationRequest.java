package com.smartjobportal.dto.application;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {

    @NotNull(message = "Job ID is required")
    private Long jobId;

    private String coverLetter;
}
