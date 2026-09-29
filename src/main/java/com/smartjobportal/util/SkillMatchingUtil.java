package com.smartjobportal.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class SkillMatchingUtil {

    private SkillMatchingUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns the list of candidate skills that match the job's required skills (case-insensitive).
     */
    public static List<String> getMatchedSkills(List<String> candidateSkills, List<String> requiredSkills) {
        if (candidateSkills == null || requiredSkills == null) return new ArrayList<>();
        List<String> normalizedCandidate = normalize(candidateSkills);
        return requiredSkills.stream()
                .filter(skill -> normalizedCandidate.contains(skill.trim().toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the list of required skills the candidate is missing.
     */
    public static List<String> getMissingSkills(List<String> candidateSkills, List<String> requiredSkills) {
        if (requiredSkills == null) return new ArrayList<>();
        if (candidateSkills == null) return new ArrayList<>(requiredSkills);
        List<String> normalizedCandidate = normalize(candidateSkills);
        return requiredSkills.stream()
                .filter(skill -> !normalizedCandidate.contains(skill.trim().toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the skill match percentage (0.0 to 100.0).
     */
    public static double calculateMatchPercentage(List<String> candidateSkills, List<String> requiredSkills) {
        if (requiredSkills == null || requiredSkills.isEmpty()) return 100.0;
        long matched = getMatchedSkills(candidateSkills, requiredSkills).size();
        return Math.round(((double) matched / requiredSkills.size()) * 100.0 * 10.0) / 10.0;
    }

    /**
     * Generates a human-readable improvement suggestion based on missing skills.
     */
    public static String generateImprovementSuggestions(List<String> missingSkills) {
        if (missingSkills == null || missingSkills.isEmpty()) {
            return "Great news! Your profile matches all required skills for this position.";
        }
        String skillList = String.join(", ", missingSkills);
        return String.format(
                "To improve your match score, consider developing the following skills: %s. " +
                "You can find online courses, certifications, or projects to demonstrate these skills.",
                skillList
        );
    }

    private static List<String> normalize(List<String> skills) {
        return skills.stream()
                .map(s -> s.trim().toLowerCase())
                .collect(Collectors.toList());
    }
}
