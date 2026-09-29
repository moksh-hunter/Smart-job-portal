package com.smartjobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resume_scores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(name = "overall_score")
    private Double overallScore;

    @Column(name = "profile_completeness_score")
    private Double profileCompletenessScore;

    @Column(name = "skill_match_score")
    private Double skillMatchScore;

    @Column(name = "experience_score")
    private Double experienceScore;

    @ElementCollection
    @CollectionTable(name = "resume_matched_skills", joinColumns = @JoinColumn(name = "score_id"))
    @Column(name = "skill")
    @Builder.Default
    private List<String> matchedSkills = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "resume_missing_skills", joinColumns = @JoinColumn(name = "score_id"))
    @Column(name = "skill")
    @Builder.Default
    private List<String> missingSkills = new ArrayList<>();

    @Column(name = "improvement_suggestions", columnDefinition = "TEXT")
    private String improvementSuggestions;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
