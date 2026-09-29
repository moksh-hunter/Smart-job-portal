package com.smartjobportal.controller;

import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.dto.resume.JobRecommendationResponse;
import com.smartjobportal.dto.common.PagedResponse;
import com.smartjobportal.entity.Application;
import com.smartjobportal.security.CustomUserDetails;
import com.smartjobportal.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recommendations", description = "Job recommendations and skill gap analysis")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Generate job recommendations for current candidate")
    public ResponseEntity<ApiResponse<List<JobRecommendationResponse>>> generateRecommendations(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(recommendationService.generateRecommendations(currentUser.getId()));
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Get paginated job recommendations for current candidate")
    public ResponseEntity<ApiResponse<PagedResponse<JobRecommendationResponse>>> getRecommendations(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(recommendationService.getRecommendations(currentUser.getId(), page, size));
    }

    @GetMapping("/skill-gap/{jobId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Analyze skill gap against a job")
    public ResponseEntity<ApiResponse<Map<String, Object>>> analyzeSkillGap(
            @PathVariable Long jobId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(recommendationService.analyzeSkillGap(currentUser.getId(), jobId));
    }

    @GetMapping("/ranked-candidates/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Get ranked candidates for a job")
    public ResponseEntity<ApiResponse<List<Application>>> getRankedCandidates(
            @PathVariable Long jobId) {
        return ResponseEntity.ok(recommendationService.getRankedCandidates(jobId));
    }
}
