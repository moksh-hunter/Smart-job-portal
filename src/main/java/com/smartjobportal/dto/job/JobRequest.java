package com.smartjobportal.dto.job;

import com.smartjobportal.entity.EmploymentType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotBlank(message = "Job title is required")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    @NotEmpty(message = "At least one required skill must be specified")
    private List<String> requiredSkills;

    @Min(value = 0)
    private Double minExperience;

    @Min(value = 0)
    private Double maxExperience;

    @Min(value = 0)
    private Double minSalary;

    @Min(value = 0)
    private Double maxSalary;

    @Size(max = 200)
    private String location;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    private LocalDate deadline;

    @Min(value = 1)
    private Integer vacancies;

    private String department;
    private String educationRequired;
    private Boolean isRemote;
}
