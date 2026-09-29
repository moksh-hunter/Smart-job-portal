package com.smartjobportal.controller;

import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.dto.resume.ResumeScoreResponse;
import com.smartjobportal.security.CustomUserDetails;
import com.smartjobportal.service.ATSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ats")
@RequiredArgsConstructor
@Tag(name = "ATS", description = "ATS score management endpoints")
public class ATSController {

    private final ATSService atsService;

    @PostMapping("/calculate/{jobId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Calculate ATS score for current user against a job")
    public ResponseEntity<ApiResponse<ResumeScoreResponse>> calculateATSScore(
            @PathVariable Long jobId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(atsService.calculateATSScore(currentUser.getId(), jobId));
    }

    @GetMapping("/score/{jobId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Get ATS score for current user for a job")
    public ResponseEntity<ApiResponse<ResumeScoreResponse>> getATSScore(
            @PathVariable Long jobId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(atsService.getATSScore(currentUser.getId(), jobId));
    }

    @GetMapping("/my-scores")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Get all ATS scores for current user")
    public ResponseEntity<ApiResponse<List<ResumeScoreResponse>>> getMyATSScores(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(atsService.getMyATSScores(currentUser.getId()));
    }

    @GetMapping("/job/{jobId}/scores")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Get all ATS scores for a job")
    public ResponseEntity<ApiResponse<List<ResumeScoreResponse>>> getATSScoresForJob(
            @PathVariable Long jobId) {
        return ResponseEntity.ok(atsService.getATSScoresForJob(jobId));
    }

    @PostMapping("/job/{jobId}/calculate-all")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Calculate and update scores for all applications of a job")
    public ResponseEntity<ApiResponse<String>> calculateAndUpdateApplicationScores(
            @PathVariable Long jobId) {
        return ResponseEntity.ok(atsService.calculateAndUpdateApplicationScores(jobId));
    }
}
