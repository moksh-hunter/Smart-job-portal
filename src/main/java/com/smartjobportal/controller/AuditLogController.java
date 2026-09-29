package com.smartjobportal.controller;

import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.dto.common.PagedResponse;
import com.smartjobportal.entity.AuditLog;
import com.smartjobportal.service.AuditLogService;
import com.smartjobportal.util.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Audit Logs", description = "Audit log management endpoints (Admin only)")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all audit logs", description = "Retrieves a paginated list of all audit logs")
    public ResponseEntity<ApiResponse<PagedResponse<AuditLog>>> getAllAuditLogs(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved", auditLogService.getAllAuditLogs(page, size)));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get audit logs by user", description = "Retrieves audit logs for a specific user")
    public ResponseEntity<ApiResponse<PagedResponse<AuditLog>>> getByUser(
            @PathVariable Long userId,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return ResponseEntity.ok(ApiResponse.success("User audit logs retrieved", auditLogService.getAuditLogsByUser(userId, page, size)));
    }

    @GetMapping("/action/{action}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get audit logs by action", description = "Retrieves audit logs for a specific action type")
    public ResponseEntity<ApiResponse<PagedResponse<AuditLog>>> getByAction(
            @PathVariable String action,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return ResponseEntity.ok(ApiResponse.success("Action audit logs retrieved", auditLogService.getAuditLogsByAction(action, page, size)));
    }

    @GetMapping("/entity/{entityType}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get audit logs by entity type", description = "Retrieves audit logs for a specific entity type")
    public ResponseEntity<ApiResponse<PagedResponse<AuditLog>>> getByEntityType(
            @PathVariable String entityType,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return ResponseEntity.ok(ApiResponse.success("Entity audit logs retrieved", auditLogService.getAuditLogsByEntityType(entityType, page, size)));
    }
}
