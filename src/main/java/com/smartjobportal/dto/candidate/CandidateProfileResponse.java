package com.smartjobportal.dto.candidate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartjobportal.entity.EmploymentType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateProfileResponse {

    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;

    // Personal
    private String headline;
    private String summary;
    private String dateOfBirth;
    private String gender;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    // Professional
    private List<String> skills;
    private Double totalExperience;
    private String currentCompany;
    private String currentDesignation;
    private Double currentSalary;
    private Double expectedSalary;
    private String noticePeriod;

    // Education
    private String highestEducation;
    private String university;
    private Integer graduationYear;

    // Links
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;

    // Resume
    private String resumeFileName;
    private Boolean hasResume;

    // Preferences
    private String preferredLocation;
    private EmploymentType employmentTypePreference;
    private Boolean isOpenToWork;
    private Integer profileCompleteness;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
