package com.smartjobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidate_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Personal Information
    @Column(length = 500)
    private String headline;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(length = 10)
    private String gender;

    @Column(length = 200)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(name = "zip_code", length = 10)
    private String zipCode;

    // Professional Information
    @ElementCollection
    @CollectionTable(name = "candidate_skills", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "skill")
    @Builder.Default
    private List<String> skills = new ArrayList<>();

    @Column(name = "total_experience")
    private Double totalExperience;

    @Column(name = "current_company", length = 200)
    private String currentCompany;

    @Column(name = "current_designation", length = 200)
    private String currentDesignation;

    @Column(name = "current_salary")
    private Double currentSalary;

    @Column(name = "expected_salary")
    private Double expectedSalary;

    @Column(name = "notice_period", length = 50)
    private String noticePeriod;

    // Education
    @Column(name = "highest_education", length = 100)
    private String highestEducation;

    @Column(name = "university", length = 200)
    private String university;

    @Column(name = "graduation_year")
    private Integer graduationYear;

    // Links
    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Column(name = "github_url", length = 500)
    private String githubUrl;

    @Column(name = "portfolio_url", length = 500)
    private String portfolioUrl;

    // Resume
    @Column(name = "resume_path", length = 500)
    private String resumePath;

    @Column(name = "resume_file_name", length = 200)
    private String resumeFileName;

    // Preferences
    @Column(name = "preferred_location", length = 200)
    private String preferredLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type_preference", length = 20)
    private EmploymentType employmentTypePreference;

    @Column(name = "is_open_to_work")
    @Builder.Default
    private Boolean isOpenToWork = true;

    @Column(name = "profile_completeness")
    @Builder.Default
    private Integer profileCompleteness = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
