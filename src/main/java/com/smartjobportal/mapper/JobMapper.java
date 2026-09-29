package com.smartjobportal.mapper;

import com.smartjobportal.dto.job.JobRequest;
import com.smartjobportal.dto.job.JobResponse;
import com.smartjobportal.entity.Job;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class JobMapper {

    public JobResponse toResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .companyId(job.getCompany().getId())
                .companyName(job.getCompany().getCompanyName())
                .companyLogo(job.getCompany().getLogoPath())
                .postedById(job.getPostedBy().getId())
                .postedByName(job.getPostedBy().getFullName())
                .title(job.getTitle())
                .description(job.getDescription())
                .requiredSkills(job.getRequiredSkills())
                .minExperience(job.getMinExperience())
                .maxExperience(job.getMaxExperience())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .location(job.getLocation())
                .employmentType(job.getEmploymentType())
                .status(job.getStatus())
                .deadline(job.getDeadline())
                .vacancies(job.getVacancies())
                .department(job.getDepartment())
                .educationRequired(job.getEducationRequired())
                .isRemote(job.getIsRemote())
                .viewsCount(job.getViewsCount())
                .applicationsCount(job.getApplicationsCount())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }

    public Job toEntity(JobRequest request) {
        return Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .requiredSkills(new ArrayList<>(request.getRequiredSkills()))
                .minExperience(request.getMinExperience())
                .maxExperience(request.getMaxExperience())
                .minSalary(request.getMinSalary())
                .maxSalary(request.getMaxSalary())
                .location(request.getLocation())
                .employmentType(request.getEmploymentType())
                .deadline(request.getDeadline())
                .vacancies(request.getVacancies() != null ? request.getVacancies() : 1)
                .department(request.getDepartment())
                .educationRequired(request.getEducationRequired())
                .isRemote(request.getIsRemote() != null ? request.getIsRemote() : false)
                .build();
    }

    public void updateEntity(Job job, JobRequest request) {
        if (request.getTitle() != null) job.setTitle(request.getTitle());
        if (request.getDescription() != null) job.setDescription(request.getDescription());
        if (request.getRequiredSkills() != null) job.setRequiredSkills(new ArrayList<>(request.getRequiredSkills()));
        if (request.getMinExperience() != null) job.setMinExperience(request.getMinExperience());
        if (request.getMaxExperience() != null) job.setMaxExperience(request.getMaxExperience());
        if (request.getMinSalary() != null) job.setMinSalary(request.getMinSalary());
        if (request.getMaxSalary() != null) job.setMaxSalary(request.getMaxSalary());
        if (request.getLocation() != null) job.setLocation(request.getLocation());
        if (request.getEmploymentType() != null) job.setEmploymentType(request.getEmploymentType());
        if (request.getDeadline() != null) job.setDeadline(request.getDeadline());
        if (request.getVacancies() != null) job.setVacancies(request.getVacancies());
        if (request.getDepartment() != null) job.setDepartment(request.getDepartment());
        if (request.getEducationRequired() != null) job.setEducationRequired(request.getEducationRequired());
        if (request.getIsRemote() != null) job.setIsRemote(request.getIsRemote());
    }
}
