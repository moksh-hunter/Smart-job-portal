package com.smartjobportal.controller;

import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.dto.admin.DashboardStatsResponse;
import com.smartjobportal.dto.admin.RecruiterDashboardResponse;
import com.smartjobportal.security.CustomUserDetails;
import com.smartjobportal.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics API", description = "Endpoints for dashboard and analytics data")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get admin dashboard", description = "Retrieves statistics for the admin dashboard")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getAdminDashboard() {
        DashboardStatsResponse stats = analyticsService.getAdminDashboard();
        return ResponseEntity.ok(ApiResponse.success("Admin dashboard statistics retrieved successfully", stats));
    }

    @GetMapping("/recruiter/dashboard")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Get recruiter dashboard", description = "Retrieves statistics for the recruiter dashboard")
    public ResponseEntity<ApiResponse<RecruiterDashboardResponse>> getRecruiterDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        RecruiterDashboardResponse stats = analyticsService.getRecruiterDashboard(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Recruiter dashboard statistics retrieved successfully", stats));
    }
}
