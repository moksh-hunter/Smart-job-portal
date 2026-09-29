package com.smartjobportal.mapper;

import com.smartjobportal.dto.candidate.CandidateProfileRequest;
import com.smartjobportal.dto.candidate.CandidateProfileResponse;
import com.smartjobportal.entity.CandidateProfile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CandidateProfileMapper {

    public CandidateProfileResponse toResponse(CandidateProfile profile) {
        return CandidateProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .fullName(profile.getUser().getFullName())
                .email(profile.getUser().getEmail())
                .phoneNumber(profile.getUser().getPhoneNumber())
                .headline(profile.getHeadline())
                .summary(profile.getSummary())
                .dateOfBirth(profile.getDateOfBirth())
                .gender(profile.getGender())
                .address(profile.getAddress())
                .city(profile.getCity())
                .state(profile.getState())
                .country(profile.getCountry())
                .zipCode(profile.getZipCode())
                .skills(profile.getSkills())
                .totalExperience(profile.getTotalExperience())
                .currentCompany(profile.getCurrentCompany())
                .currentDesignation(profile.getCurrentDesignation())
                .currentSalary(profile.getCurrentSalary())
                .expectedSalary(profile.getExpectedSalary())
                .noticePeriod(profile.getNoticePeriod())
                .highestEducation(profile.getHighestEducation())
                .university(profile.getUniversity())
                .graduationYear(profile.getGraduationYear())
                .linkedinUrl(profile.getLinkedinUrl())
                .githubUrl(profile.getGithubUrl())
                .portfolioUrl(profile.getPortfolioUrl())
                .resumeFileName(profile.getResumeFileName())
                .hasResume(profile.getResumePath() != null)
                .preferredLocation(profile.getPreferredLocation())
                .employmentTypePreference(profile.getEmploymentTypePreference())
                .isOpenToWork(profile.getIsOpenToWork())
                .profileCompleteness(profile.getProfileCompleteness())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    public void updateEntity(CandidateProfile profile, CandidateProfileRequest request) {
        if (request.getHeadline() != null) profile.setHeadline(request.getHeadline());
        if (request.getSummary() != null) profile.setSummary(request.getSummary());
        if (request.getDateOfBirth() != null) profile.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null) profile.setGender(request.getGender());
        if (request.getAddress() != null) profile.setAddress(request.getAddress());
        if (request.getCity() != null) profile.setCity(request.getCity());
        if (request.getState() != null) profile.setState(request.getState());
        if (request.getCountry() != null) profile.setCountry(request.getCountry());
        if (request.getZipCode() != null) profile.setZipCode(request.getZipCode());
        if (request.getSkills() != null) profile.setSkills(new ArrayList<>(request.getSkills()));
        if (request.getTotalExperience() != null) profile.setTotalExperience(request.getTotalExperience());
        if (request.getCurrentCompany() != null) profile.setCurrentCompany(request.getCurrentCompany());
        if (request.getCurrentDesignation() != null) profile.setCurrentDesignation(request.getCurrentDesignation());
        if (request.getCurrentSalary() != null) profile.setCurrentSalary(request.getCurrentSalary());
        if (request.getExpectedSalary() != null) profile.setExpectedSalary(request.getExpectedSalary());
        if (request.getNoticePeriod() != null) profile.setNoticePeriod(request.getNoticePeriod());
        if (request.getHighestEducation() != null) profile.setHighestEducation(request.getHighestEducation());
        if (request.getUniversity() != null) profile.setUniversity(request.getUniversity());
        if (request.getGraduationYear() != null) profile.setGraduationYear(request.getGraduationYear());
        if (request.getLinkedinUrl() != null) profile.setLinkedinUrl(request.getLinkedinUrl());
        if (request.getGithubUrl() != null) profile.setGithubUrl(request.getGithubUrl());
        if (request.getPortfolioUrl() != null) profile.setPortfolioUrl(request.getPortfolioUrl());
        if (request.getPreferredLocation() != null) profile.setPreferredLocation(request.getPreferredLocation());
        if (request.getEmploymentTypePreference() != null) profile.setEmploymentTypePreference(request.getEmploymentTypePreference());
        if (request.getIsOpenToWork() != null) profile.setIsOpenToWork(request.getIsOpenToWork());
    }

    public int calculateProfileCompleteness(CandidateProfile profile) {
        int score = 0;
        int total = 20;

        if (profile.getHeadline() != null && !profile.getHeadline().isBlank()) score++;
        if (profile.getSummary() != null && !profile.getSummary().isBlank()) score++;
        if (profile.getAddress() != null) score++;
        if (profile.getCity() != null) score++;
        if (profile.getCountry() != null) score++;
        if (profile.getSkills() != null && !profile.getSkills().isEmpty()) score += 2;
        if (profile.getTotalExperience() != null) score++;
        if (profile.getCurrentCompany() != null) score++;
        if (profile.getCurrentDesignation() != null) score++;
        if (profile.getHighestEducation() != null) score++;
        if (profile.getUniversity() != null) score++;
        if (profile.getGraduationYear() != null) score++;
        if (profile.getLinkedinUrl() != null) score++;
        if (profile.getGithubUrl() != null) score++;
        if (profile.getPortfolioUrl() != null) score++;
        if (profile.getResumePath() != null) score += 2;
        if (profile.getPreferredLocation() != null) score++;
        if (profile.getExpectedSalary() != null) score++;

        return (int) ((score / (double) total) * 100);
    }
}
