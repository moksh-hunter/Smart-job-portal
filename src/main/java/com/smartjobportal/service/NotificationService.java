package com.smartjobportal.service;

import com.smartjobportal.dto.common.PagedResponse;
import com.smartjobportal.dto.notification.NotificationResponse;
import com.smartjobportal.entity.Notification;
import com.smartjobportal.entity.NotificationType;
import com.smartjobportal.entity.User;
import com.smartjobportal.exception.ResourceNotFoundException;
import com.smartjobportal.exception.UnauthorizedException;
import com.smartjobportal.mapper.NotificationMapper;
import com.smartjobportal.repository.NotificationRepository;
import com.smartjobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Transactional
    public void createNotification(Long userId, String title, String message, NotificationType type, Long referenceId, String referenceType) {
        log.info("Creating notification for user {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(type)
                .referenceId(referenceId)
                .referenceType(referenceType)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public PagedResponse<NotificationResponse> getNotifications(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        List<NotificationResponse> content = notifications.getContent().stream()
                .map(notificationMapper::toResponse)
                .toList();

        return PagedResponse.<NotificationResponse>builder()
                .content(content)
                .pageNumber(notifications.getNumber())
                .pageSize(notifications.getSize())
                .totalElements(notifications.getTotalElements())
                .totalPages(notifications.getTotalPages())
                .last(notifications.isLast())
                .first(notifications.isFirst())
                .build();
    }

    @Transactional(readOnly = true)
    public PagedResponse<NotificationResponse> getUnreadNotifications(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId, pageable);
        List<NotificationResponse> content = notifications.getContent().stream()
                .map(notificationMapper::toResponse)
                .toList();

        return PagedResponse.<NotificationResponse>builder()
                .content(content)
                .pageNumber(notifications.getNumber())
                .pageSize(notifications.getSize())
                .totalElements(notifications.getTotalElements())
                .totalPages(notifications.getTotalPages())
                .last(notifications.isLast())
                .first(notifications.isFirst())
                .build();
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markAsRead(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));

        if (!notification.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized to access this notification");
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }
}
