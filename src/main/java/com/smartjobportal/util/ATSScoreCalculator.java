package com.smartjobportal.util;

import com.smartjobportal.entity.CandidateProfile;
import com.smartjobportal.entity.Job;

import java.util.List;

public final class ATSScoreCalculator {

    private ATSScoreCalculator() {
        throw new IllegalStateException("Utility class");
    }

    // Score weights (must total 100)
    private static final double PROFILE_COMPLETENESS_WEIGHT = 0.20;
    private static final double SKILL_MATCH_WEIGHT          = 0.45;
    private static final double EXPERIENCE_WEIGHT           = 0.25;
    private static final double EDUCATION_WEIGHT            = 0.10;

    /**
     * Calculates the ATS overall score (0–100) for a candidate against a job.
     *
     * @param profile  The candidate's profile
     * @param job      The job being applied for
     * @return         ATS overall score rounded to 1 decimal place
     */
    public static double calculate(CandidateProfile profile, Job job) {
        double profileScore    = calculateProfileCompletenessScore(profile);
        double skillScore      = calculateSkillMatchScore(profile.getSkills(), job.getRequiredSkills());
        double experienceScore = calculateExperienceScore(profile.getTotalExperience(), job.getMinExperience(), job.getMaxExperience());
        double educationScore  = calculateEducationScore(profile.getHighestEducation(), job.getEducationRequired());

        double overall = (profileScore * PROFILE_COMPLETENESS_WEIGHT)
                + (skillScore * SKILL_MATCH_WEIGHT)
                + (experienceScore * EXPERIENCE_WEIGHT)
                + (educationScore * EDUCATION_WEIGHT);

        return Math.round(overall * 10.0) / 10.0;
    }

    /**
     * Calculates profile completeness score (0–100).
     * Uses the already-computed profileCompleteness field on the entity.
     */
    public static double calculateProfileCompletenessScore(CandidateProfile profile) {
        if (profile.getProfileCompleteness() == null) return 0.0;
        return profile.getProfileCompleteness().doubleValue();
    }

    /**
     * Calculates skill match score (0–100) using SkillMatchingUtil.
     */
    public static double calculateSkillMatchScore(List<String> candidateSkills, List<String> requiredSkills) {
        return SkillMatchingUtil.calculateMatchPercentage(candidateSkills, requiredSkills);
    }

    /**
     * Calculates experience score (0–100).
     * Full score if within required range. Partial score for underqualified.
     */
    public static double calculateExperienceScore(Double candidateExp, Double minRequired, Double maxRequired) {
        if (candidateExp == null) return 0.0;
        if (minRequired == null || minRequired == 0) return 100.0;

        if (candidateExp >= minRequired) {
            // Full score if in range; capped at 100 even if overqualified
            return 100.0;
        }
        // Partial score proportional to experience ratio
        double ratio = candidateExp / minRequired;
        return Math.round(ratio * 100.0 * 10.0) / 10.0;
    }

    /**
     * Calculates education score (0 or 100) — simple presence check.
     */
    public static double calculateEducationScore(String candidateEducation, String requiredEducation) {
        if (requiredEducation == null || requiredEducation.isBlank()) return 100.0;
        if (candidateEducation == null || candidateEducation.isBlank()) return 0.0;
        return candidateEducation.toLowerCase().contains(requiredEducation.toLowerCase()) ? 100.0 : 50.0;
    }

    /**
     * Calculates ranking score used for sorting applicants (0–100).
     * Composite of ATS score + experience bonus.
     */
    public static double calculateRankingScore(double atsScore, Double candidateExperience) {
        double expBonus = 0.0;
        if (candidateExperience != null && candidateExperience > 0) {
            // Up to 10 bonus points for 10+ years experience
            expBonus = Math.min(candidateExperience, 10.0);
        }
        return Math.min(Math.round((atsScore + expBonus) * 10.0) / 10.0, 100.0);
    }
}
