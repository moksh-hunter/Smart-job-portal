package com.smartjobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "interviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "interview_date", nullable = false)
    private LocalDate interviewDate;

    @Column(name = "interview_time", nullable = false)
    private LocalTime interviewTime;

    @Column(name = "round", nullable = false, length = 100)
    private String round;

    @Column(name = "interviewer_name", length = 200)
    private String interviewerName;

    @Column(name = "interviewer_email", length = 200)
    private String interviewerEmail;

    @Column(name = "meeting_link", length = 500)
    private String meetingLink;

    @Column(name = "location", length = 300)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private InterviewStatus status = InterviewStatus.SCHEDULED;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
