package com.smartjobportal.controller;

import com.smartjobportal.dto.interview.InterviewFeedbackRequest;
import com.smartjobportal.dto.interview.InterviewRequest;
import com.smartjobportal.dto.interview.InterviewResponse;
import com.smartjobportal.security.CustomUserDetails;
import com.smartjobportal.service.InterviewService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
@Tag(name = "Interview", description = "Interview management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Schedule interview", description = "Schedule a new interview for a candidate")
    public ResponseEntity<InterviewResponse> scheduleInterview(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody InterviewRequest request) {
        InterviewResponse response = interviewService.scheduleInterview(currentUser.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Update interview", description = "Update scheduled interview details")
    public ResponseEntity<InterviewResponse> updateInterview(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id,
            @Valid @RequestBody InterviewRequest request) {
        return ResponseEntity.ok(interviewService.updateInterview(currentUser.getId(), id, request));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Cancel interview", description = "Cancel a scheduled interview")
    public ResponseEntity<InterviewResponse> cancelInterview(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(interviewService.cancelInterview(currentUser.getId(), id));
    }

    @PatchMapping("/{id}/feedback")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Submit feedback", description = "Submit feedback and mark interview as completed")
    public ResponseEntity<InterviewResponse> submitFeedback(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id,
            @Valid @RequestBody InterviewFeedbackRequest request) {
        return ResponseEntity.ok(interviewService.submitFeedback(currentUser.getId(), id, request));
    }

    @GetMapping("/application/{applicationId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get application interviews", description = "Get all interviews for an application")
    public ResponseEntity<List<InterviewResponse>> getInterviewsByApplication(
            @PathVariable Long applicationId) {
        return ResponseEntity.ok(interviewService.getInterviewsByApplication(applicationId));
    }

    @GetMapping("/my-interviews")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Get candidate interviews", description = "Get all interviews for the authenticated candidate")
    public ResponseEntity<List<InterviewResponse>> getMyInterviews(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(interviewService.getMyInterviews(currentUser.getId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get interview by ID", description = "Retrieve single interview details")
    public ResponseEntity<InterviewResponse> getInterviewById(@PathVariable Long id) {
        return ResponseEntity.ok(interviewService.getInterviewById(id));
    }
}
