package com.smartjobportal.controller;

import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.dto.notification.NotificationResponse;
import com.smartjobportal.dto.common.PagedResponse;
import com.smartjobportal.security.CustomUserDetails;
import com.smartjobportal.service.NotificationService;
import com.smartjobportal.util.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "Endpoints for managing notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get user notifications", description = "Retrieves a paginated list of all notifications for the authenticated user")
    public ResponseEntity<ApiResponse<PagedResponse<NotificationResponse>>> getNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        PagedResponse<NotificationResponse> response = notificationService.getNotifications(userDetails.getId(), page, size);
        return ResponseEntity.ok(ApiResponse.success("Notifications retrieved successfully", response));
    }

    @GetMapping("/unread")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get unread notifications", description = "Retrieves a paginated list of unread notifications for the authenticated user")
    public ResponseEntity<ApiResponse<PagedResponse<NotificationResponse>>> getUnreadNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        PagedResponse<NotificationResponse> response = notificationService.getUnreadNotifications(userDetails.getId(), page, size);
        return ResponseEntity.ok(ApiResponse.success("Unread notifications retrieved successfully", response));
    }

    @GetMapping("/unread/count")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get unread notifications count", description = "Retrieves the count of unread notifications for the authenticated user")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        long count = notificationService.getUnreadCount(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Unread notification count retrieved successfully", count));
    }

    @PatchMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Mark notification as read", description = "Marks a specific notification as read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        notificationService.markAsRead(userDetails.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read"));
    }

    @PatchMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Mark all notifications as read", description = "Marks all unread notifications as read for the authenticated user")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(@AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.markAllAsRead(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read"));
    }
}
