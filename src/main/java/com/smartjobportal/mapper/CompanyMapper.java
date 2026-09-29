package com.smartjobportal.mapper;

import com.smartjobportal.dto.company.CompanyRequest;
import com.smartjobportal.dto.company.CompanyResponse;
import com.smartjobportal.entity.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public CompanyResponse toResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .recruiterId(company.getRecruiter().getId())
                .recruiterName(company.getRecruiter().getFullName())
                .companyName(company.getCompanyName())
                .website(company.getWebsite())
                .industry(company.getIndustry())
                .description(company.getDescription())
                .location(company.getLocation())
                .companySize(company.getCompanySize())
                .foundedYear(company.getFoundedYear())
                .email(company.getEmail())
                .phoneNumber(company.getPhoneNumber())
                .isVerified(company.getIsVerified())
                .totalJobs(company.getJobs() != null ? company.getJobs().size() : 0)
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    public Company toEntity(CompanyRequest request) {
        return Company.builder()
                .companyName(request.getCompanyName())
                .website(request.getWebsite())
                .industry(request.getIndustry())
                .description(request.getDescription())
                .location(request.getLocation())
                .companySize(request.getCompanySize())
                .foundedYear(request.getFoundedYear())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }

    public void updateEntity(Company company, CompanyRequest request) {
        if (request.getCompanyName() != null) company.setCompanyName(request.getCompanyName());
        if (request.getWebsite() != null) company.setWebsite(request.getWebsite());
        if (request.getIndustry() != null) company.setIndustry(request.getIndustry());
        if (request.getDescription() != null) company.setDescription(request.getDescription());
        if (request.getLocation() != null) company.setLocation(request.getLocation());
        if (request.getCompanySize() != null) company.setCompanySize(request.getCompanySize());
        if (request.getFoundedYear() != null) company.setFoundedYear(request.getFoundedYear());
        if (request.getEmail() != null) company.setEmail(request.getEmail());
        if (request.getPhoneNumber() != null) company.setPhoneNumber(request.getPhoneNumber());
    }
}
