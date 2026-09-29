package com.smartjobportal.controller;

import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.dto.job.JobRequest;
import com.smartjobportal.dto.job.JobResponse;
import com.smartjobportal.dto.common.PagedResponse;
import com.smartjobportal.entity.EmploymentType;
import com.smartjobportal.entity.JobStatus;
import com.smartjobportal.security.CustomUserDetails;
import com.smartjobportal.service.JobService;
import com.smartjobportal.util.AppConstants;
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
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
@Tag(name = "Job Management", description = "APIs for managing and searching jobs")
public class JobController {

    private final JobService jobService;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Create a new job", description = "Allows recruiters to post a new job")
    public ResponseEntity<ApiResponse<JobResponse>> createJob(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody JobRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobService.createJob(currentUser.getId(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Update an existing job", description = "Allows recruiters to update their job details")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id,
            @Valid @RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.updateJob(currentUser.getId(), id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job by ID", description = "Public endpoint to view job details")
    public ResponseEntity<ApiResponse<JobResponse>> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search jobs", description = "Public endpoint to search and filter open jobs")
    public ResponseEntity<ApiResponse<PagedResponse<JobResponse>>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) EmploymentType employmentType,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary,
            @RequestParam(required = false) Double experience,
            @RequestParam(required = false) Boolean isRemote,
            @RequestParam(required = false) List<String> skills,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        
        return ResponseEntity.ok(jobService.searchJobs(
                keyword, location, companyId, employmentType, minSalary, maxSalary, 
                experience, isRemote, skills, page, size, sortBy, sortDir));
    }

    @GetMapping("/recruiter")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Get recruiter's jobs", description = "Fetches jobs posted by the authenticated recruiter")
    public ResponseEntity<ApiResponse<PagedResponse<JobResponse>>> getMyJobs(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(required = false) JobStatus status,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size) {
        return ResponseEntity.ok(jobService.getJobsByRecruiter(currentUser.getId(), status, page, size));
    }

    @PatchMapping("/{id}/close")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Close a job", description = "Allows recruiters to close an open job")
    public ResponseEntity<ApiResponse<Void>> closeJob(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(jobService.closeJob(currentUser.getId(), id));
    }

    @PatchMapping("/{id}/reopen")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Reopen a job", description = "Allows recruiters to reopen a closed job")
    public ResponseEntity<ApiResponse<Void>> reopenJob(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(jobService.reopenJob(currentUser.getId(), id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Delete a job", description = "Allows recruiters to delete a job permanently")
    public ResponseEntity<ApiResponse<Void>> deleteJob(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(jobService.deleteJob(currentUser.getId(), id));
    }
}
