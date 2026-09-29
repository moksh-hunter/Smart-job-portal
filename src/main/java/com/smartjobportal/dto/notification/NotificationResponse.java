package com.smartjobportal.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartjobportal.entity.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private Boolean isRead;
    private Long referenceId;
    private String referenceType;
    private LocalDateTime createdAt;
}
