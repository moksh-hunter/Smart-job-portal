package com.smartjobportal.dto.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartjobportal.entity.EmploymentType;
import com.smartjobportal.entity.JobStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobResponse {
    private Long id;
    private Long companyId;
    private String companyName;
    private String companyLogo;
    private Long postedById;
    private String postedByName;
    private String title;
    private String description;
    private List<String> requiredSkills;
    private Double minExperience;
    private Double maxExperience;
    private Double minSalary;
    private Double maxSalary;
    private String location;
    private EmploymentType employmentType;
    private JobStatus status;
    private LocalDate deadline;
    private Integer vacancies;
    private String department;
    private String educationRequired;
    private Boolean isRemote;
    private Integer viewsCount;
    private Integer applicationsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
