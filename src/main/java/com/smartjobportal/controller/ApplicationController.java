package com.smartjobportal.controller;

import com.smartjobportal.dto.application.ApplicationRequest;
import com.smartjobportal.dto.application.ApplicationStatusUpdateRequest;
import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.dto.application.ApplicationResponse;
import com.smartjobportal.dto.job.JobResponse;
import com.smartjobportal.dto.common.PagedResponse;
import com.smartjobportal.security.CustomUserDetails;
import com.smartjobportal.service.ApplicationService;
import com.smartjobportal.util.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
@Tag(name = "Application", description = "Application management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Apply for a job", description = "Submit a job application")
    public ResponseEntity<ApplicationResponse> applyForJob(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody ApplicationRequest request) {
        ApplicationResponse response = applicationService.applyForJob(currentUser.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Get candidate applications", description = "Get paginated list of candidate's applications")
    public ResponseEntity<PagedResponse<ApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size) {
        return ResponseEntity.ok(applicationService.getMyApplications(currentUser.getId(), page, size));
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Get job applications", description = "Get paginated list of applications for a specific job")
    public ResponseEntity<PagedResponse<ApplicationResponse>> getApplicationsForJob(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long jobId,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size) {
        return ResponseEntity.ok(applicationService.getApplicationsForJob(currentUser.getId(), jobId, page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get application by ID", description = "Retrieve single application details")
    public ResponseEntity<ApplicationResponse> getApplicationById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Update application status", description = "Update the status of an application")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusUpdateRequest request) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(currentUser.getId(), id, request));
    }

    @PatchMapping("/{id}/withdraw")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Withdraw application", description = "Withdraw a submitted application")
    public ResponseEntity<ApplicationResponse> withdrawApplication(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(applicationService.withdrawApplication(currentUser.getId(), id));
    }

    @PostMapping("/save/{jobId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Save job", description = "Bookmark a job for later")
    public ResponseEntity<ApiResponse<Void>> saveJob(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long jobId) {
        applicationService.saveJob(currentUser.getId(), jobId);
        return ResponseEntity.ok(ApiResponse.success("Job saved successfully"));
    }

    @DeleteMapping("/save/{jobId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Unsave job", description = "Remove a bookmarked job")
    public ResponseEntity<ApiResponse<Void>> unsaveJob(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long jobId) {
        applicationService.unsaveJob(currentUser.getId(), jobId);
        return ResponseEntity.ok(ApiResponse.success("Job removed from saved list"));
    }

    @GetMapping("/saved")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Get saved jobs", description = "Get paginated list of saved jobs")
    public ResponseEntity<PagedResponse<JobResponse>> getSavedJobs(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size) {
        return ResponseEntity.ok(applicationService.getSavedJobs(currentUser.getId(), page, size));
    }

    @GetMapping("/job/{jobId}/ranked")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Get ranked applicants", description = "Get applications ordered by ranking score")
    public ResponseEntity<PagedResponse<ApplicationResponse>> getRankedApplicants(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long jobId,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size) {
        return ResponseEntity.ok(applicationService.getRankedApplicants(currentUser.getId(), jobId, page, size));
    }
}
