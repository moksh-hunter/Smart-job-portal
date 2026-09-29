package com.smartjobportal.service;

import com.smartjobportal.dto.common.PagedResponse;
import com.smartjobportal.entity.AuditLog;
import com.smartjobportal.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Transactional
    public void logAction(Long userId, String userEmail, String action, String entityType, Long entityId, String details, String ipAddress) {
        log.debug("Logging audit action: {}", action);
        AuditLog auditLog = new AuditLog();
        auditLog.setUserId(userId);
        auditLog.setUserEmail(userEmail);
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setDetails(details);
        auditLog.setIpAddress(ipAddress);
        
        auditLogRepository.save(auditLog);
    }

    @Transactional(readOnly = true)
    public PagedResponse<AuditLog> getAuditLogsByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> auditLogs = auditLogRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return buildPagedResponse(auditLogs);
    }

    @Transactional(readOnly = true)
    public PagedResponse<AuditLog> getAuditLogsByAction(String action, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> auditLogs = auditLogRepository.findByActionOrderByCreatedAtDesc(action, pageable);
        return buildPagedResponse(auditLogs);
    }

    @Transactional(readOnly = true)
    public PagedResponse<AuditLog> getAuditLogsByEntityType(String entityType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> auditLogs = auditLogRepository.findByEntityTypeOrderByCreatedAtDesc(entityType, pageable);
        return buildPagedResponse(auditLogs);
    }

    @Transactional(readOnly = true)
    public PagedResponse<AuditLog> getAllAuditLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> auditLogs = auditLogRepository.findAll(pageable);
        return buildPagedResponse(auditLogs);
    }

    private PagedResponse<AuditLog> buildPagedResponse(Page<AuditLog> page) {
        return PagedResponse.<AuditLog>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .build();
    }
}
