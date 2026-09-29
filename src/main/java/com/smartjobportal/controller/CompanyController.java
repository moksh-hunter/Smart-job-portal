package com.smartjobportal.controller;

import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.dto.company.CompanyRequest;
import com.smartjobportal.dto.company.CompanyResponse;
import com.smartjobportal.security.CustomUserDetails;
import com.smartjobportal.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Tag(name = "Company Management", description = "APIs for managing companies")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Create a new company", description = "Allows recruiters to register a new company")
    public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(companyService.createCompany(currentUser.getId(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Update an existing company", description = "Allows recruiters to update their company details")
    public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(companyService.updateCompany(currentUser.getId(), id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get company by ID", description = "Public endpoint to view company details")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @GetMapping("/my-companies")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Get current recruiter's companies", description = "Fetches all companies owned by the authenticated recruiter")
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> getMyCompanies(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(companyService.getCompaniesByRecruiter(currentUser.getId()));
    }

    @GetMapping
    @Operation(summary = "Get all companies", description = "Public endpoint to view all companies")
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Delete a company", description = "Allows recruiters to delete their company")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(companyService.deleteCompany(currentUser.getId(), id));
    }
}
