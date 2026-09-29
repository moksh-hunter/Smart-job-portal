package com.smartjobportal.service;

import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.dto.company.CompanyRequest;
import com.smartjobportal.dto.company.CompanyResponse;
import com.smartjobportal.entity.Company;
import com.smartjobportal.entity.User;
import com.smartjobportal.exception.BadRequestException;
import com.smartjobportal.exception.DuplicateResourceException;
import com.smartjobportal.exception.ResourceNotFoundException;
import com.smartjobportal.mapper.CompanyMapper;
import com.smartjobportal.repository.CompanyRepository;
import com.smartjobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;

    @Transactional
    public ApiResponse<CompanyResponse> createCompany(Long userId, CompanyRequest request) {
        log.info("Creating new company '{}' for user ID: {}", request.getCompanyName(), userId);
        
        if (companyRepository.existsByCompanyNameIgnoreCase(request.getCompanyName())) {
            throw new DuplicateResourceException("Company with name '" + request.getCompanyName() + "' already exists");
        }

        User recruiter = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Company company = companyMapper.toEntity(request);
        company.setRecruiter(recruiter);
        company.setIsVerified(false);

        Company savedCompany = companyRepository.save(company);
        return ApiResponse.success("Company created successfully", companyMapper.toResponse(savedCompany));
    }

    @Transactional
    public ApiResponse<CompanyResponse> updateCompany(Long userId, Long companyId, CompanyRequest request) {
        log.info("Updating company ID: {} by user ID: {}", companyId, userId);
        
        Company company = getCompanyAndVerifyOwner(userId, companyId);
        
        if (!company.getCompanyName().equalsIgnoreCase(request.getCompanyName()) && 
            companyRepository.existsByCompanyNameIgnoreCase(request.getCompanyName())) {
            throw new DuplicateResourceException("Company with name '" + request.getCompanyName() + "' already exists");
        }

        companyMapper.updateEntity(company, request);
        Company updatedCompany = companyRepository.save(company);
        
        return ApiResponse.success("Company updated successfully", companyMapper.toResponse(updatedCompany));
    }

    @Transactional(readOnly = true)
    public ApiResponse<CompanyResponse> getCompanyById(Long companyId) {
        log.info("Fetching company ID: {}", companyId);
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));
        return ApiResponse.success("Company fetched successfully", companyMapper.toResponse(company));
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<CompanyResponse>> getCompaniesByRecruiter(Long userId) {
        log.info("Fetching companies for recruiter ID: {}", userId);
        List<Company> companies = companyRepository.findByRecruiterId(userId);
        List<CompanyResponse> responses = companies.stream()
                .map(companyMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success("Companies fetched successfully", responses);
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<CompanyResponse>> getAllCompanies() {
        log.info("Fetching all companies");
        List<Company> companies = companyRepository.findAll();
        List<CompanyResponse> responses = companies.stream()
                .map(companyMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success("All companies fetched successfully", responses);
    }

    @Transactional
    public ApiResponse<Void> deleteCompany(Long userId, Long companyId) {
        log.info("Deleting company ID: {} by user ID: {}", companyId, userId);
        Company company = getCompanyAndVerifyOwner(userId, companyId);
        companyRepository.delete(company);
        return ApiResponse.success("Company deleted successfully");
    }
    
    private Company getCompanyAndVerifyOwner(Long userId, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));
                
        if (!company.getRecruiter().getId().equals(userId)) {
            throw new BadRequestException("You don't have permission to modify this company");
        }
        return company;
    }
}
