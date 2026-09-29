package com.smartjobportal.controller;

import com.smartjobportal.dto.candidate.CandidateProfileRequest;
import com.smartjobportal.dto.candidate.CandidateProfileResponse;
import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.security.CustomUserDetails;
import com.smartjobportal.service.CandidateProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/v1/candidates/profile")
@RequiredArgsConstructor
@Tag(name = "Candidate Profile", description = "Candidate profile management endpoints")
public class CandidateProfileController {

    private final CandidateProfileService profileService;

    @GetMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Get my profile", description = "Retrieves authenticated candidate's profile")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(profileService.getProfile(currentUser.getId()));
    }

    @GetMapping("/{profileId}")
    @Operation(summary = "Get profile by ID", description = "Retrieves a candidate profile by its ID")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> getProfileById(
            @PathVariable Long profileId) {
        return ResponseEntity.ok(profileService.getProfileById(profileId));
    }

    @PutMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Create or update profile", description = "Creates or updates candidate's profile")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> createOrUpdateProfile(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody CandidateProfileRequest request) {
        return ResponseEntity.ok(profileService.createOrUpdateProfile(currentUser.getId(), request));
    }

    @PostMapping("/resume")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Upload resume", description = "Upload resume in PDF format (max 5MB)")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> uploadResume(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(profileService.uploadResume(currentUser.getId(), file));
    }

    @GetMapping("/resume/download")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Download resume", description = "Download candidate's uploaded resume")
    public ResponseEntity<Resource> downloadResume(
            @AuthenticationPrincipal CustomUserDetails currentUser) throws MalformedURLException {
        Path path = profileService.getResumePath(currentUser.getId());
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName() + "\"")
                .body(resource);
    }
}
