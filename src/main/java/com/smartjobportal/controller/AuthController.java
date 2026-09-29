package com.smartjobportal.controller;

import com.smartjobportal.dto.auth.*;
import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.security.CustomUserDetails;
import com.smartjobportal.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and authorization endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/candidate")
    @Operation(summary = "Register a new candidate", description = "Creates a new candidate account and sends email verification")
    public ResponseEntity<ApiResponse<AuthResponse>> registerCandidate(
            @Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authService.registerCandidate(request), HttpStatus.CREATED);
    }

    @PostMapping("/register/recruiter")
    @Operation(summary = "Register a new recruiter", description = "Creates a new recruiter account and sends email verification")
    public ResponseEntity<ApiResponse<AuthResponse>> registerRecruiter(
            @Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authService.registerRecruiter(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates user and returns JWT tokens")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token", description = "Generates new access token using refresh token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Invalidates refresh token and logs out user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(authService.logout(currentUser.getId()));
    }

    @GetMapping("/verify-email")
    @Operation(summary = "Verify email", description = "Verifies user's email address using token")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password", description = "Changes password for authenticated user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(authService.changePassword(currentUser.getId(), request));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Sends password reset email")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(authService.forgotPassword(request));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Resets password using token")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }

    @PostMapping("/resend-verification")
    @Operation(summary = "Resend verification email", description = "Resends email verification link")
    public ResponseEntity<ApiResponse<Void>> resendVerification(@RequestParam String email) {
        return ResponseEntity.ok(authService.resendVerificationEmail(email));
    }
}
