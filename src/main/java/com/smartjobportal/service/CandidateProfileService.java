package com.smartjobportal.service;

import com.smartjobportal.dto.candidate.CandidateProfileRequest;
import com.smartjobportal.dto.candidate.CandidateProfileResponse;
import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.entity.CandidateProfile;
import com.smartjobportal.entity.User;
import com.smartjobportal.exception.BadRequestException;
import com.smartjobportal.exception.ResourceNotFoundException;
import com.smartjobportal.mapper.CandidateProfileMapper;
import com.smartjobportal.repository.CandidateProfileRepository;
import com.smartjobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateProfileService {

    private final CandidateProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final CandidateProfileMapper profileMapper;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public ApiResponse<CandidateProfileResponse> getProfile(Long userId) {
        CandidateProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CandidateProfile", "userId", userId));
        return ApiResponse.success("Profile retrieved successfully", profileMapper.toResponse(profile));
    }

    @Transactional(readOnly = true)
    public ApiResponse<CandidateProfileResponse> getProfileById(Long profileId) {
        CandidateProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("CandidateProfile", "id", profileId));
        return ApiResponse.success("Profile retrieved successfully", profileMapper.toResponse(profile));
    }

    @Transactional
    public ApiResponse<CandidateProfileResponse> createOrUpdateProfile(Long userId, CandidateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        CandidateProfile profile = profileRepository.findByUserId(userId)
                .orElse(CandidateProfile.builder().user(user).build());

        profileMapper.updateEntity(profile, request);
        profile.setProfileCompleteness(profileMapper.calculateProfileCompleteness(profile));

        CandidateProfile saved = profileRepository.save(profile);
        log.info("Profile updated for user: {}", userId);

        return ApiResponse.success("Profile updated successfully", profileMapper.toResponse(saved));
    }

    @Transactional
    public ApiResponse<CandidateProfileResponse> uploadResume(Long userId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Resume file is required");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new BadRequestException("Only PDF files are allowed");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BadRequestException("File size must not exceed 5MB");
        }

        CandidateProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CandidateProfile", "userId", userId));

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Delete old resume if exists
            if (profile.getResumePath() != null) {
                Path oldFile = Paths.get(profile.getResumePath());
                Files.deleteIfExists(oldFile);
            }

            profile.setResumePath(filePath.toString());
            profile.setResumeFileName(file.getOriginalFilename());
            profile.setProfileCompleteness(profileMapper.calculateProfileCompleteness(profile));

            CandidateProfile saved = profileRepository.save(profile);
            log.info("Resume uploaded for user: {}", userId);

            return ApiResponse.success("Resume uploaded successfully", profileMapper.toResponse(saved));
        } catch (IOException e) {
            throw new BadRequestException("Failed to upload resume: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Path getResumePath(Long userId) {
        CandidateProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CandidateProfile", "userId", userId));

        if (profile.getResumePath() == null) {
            throw new ResourceNotFoundException("Resume not found for user: " + userId);
        }

        return Paths.get(profile.getResumePath());
    }
}
