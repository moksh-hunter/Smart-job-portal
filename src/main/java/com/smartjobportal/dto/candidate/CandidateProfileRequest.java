package com.smartjobportal.dto.candidate;

import com.smartjobportal.entity.EmploymentType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileRequest {

    @Size(max = 500, message = "Headline must not exceed 500 characters")
    private String headline;

    private String summary;
    private String dateOfBirth;
    private String gender;

    @Size(max = 200)
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    private List<String> skills;

    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 50, message = "Experience cannot exceed 50 years")
    private Double totalExperience;

    private String currentCompany;
    private String currentDesignation;

    @Min(value = 0)
    private Double currentSalary;

    @Min(value = 0)
    private Double expectedSalary;

    private String noticePeriod;
    private String highestEducation;
    private String university;
    private Integer graduationYear;

    @Pattern(regexp = "^https?://.*", message = "Invalid URL format")
    private String linkedinUrl;

    @Pattern(regexp = "^https?://.*", message = "Invalid URL format")
    private String githubUrl;

    @Pattern(regexp = "^https?://.*", message = "Invalid URL format")
    private String portfolioUrl;

    private String preferredLocation;
    private EmploymentType employmentTypePreference;
    private Boolean isOpenToWork;
}
