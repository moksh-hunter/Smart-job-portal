package com.smartjobportal.service;

import com.smartjobportal.dto.auth.*;
import com.smartjobportal.dto.common.ApiResponse;
import com.smartjobportal.entity.Role;
import com.smartjobportal.entity.RoleName;
import com.smartjobportal.entity.User;
import com.smartjobportal.exception.*;
import com.smartjobportal.repository.RoleRepository;
import com.smartjobportal.repository.UserRepository;
import com.smartjobportal.security.CustomUserDetails;
import com.smartjobportal.security.JwtTokenProvider;
import com.smartjobportal.util.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    /**
     * Register a new candidate user.
     */
    @Transactional
    public ApiResponse<AuthResponse> registerCandidate(RegisterRequest request) {
        return registerUser(request, RoleName.ROLE_CANDIDATE);
    }

    /**
     * Register a new recruiter user.
     */
    @Transactional
    public ApiResponse<AuthResponse> registerRecruiter(RegisterRequest request) {
        return registerUser(request, RoleName.ROLE_RECRUITER);
    }

    /**
     * Common registration logic.
     */
    private ApiResponse<AuthResponse> registerUser(RegisterRequest request, RoleName roleName) {
        // Check for duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already registered: " + request.getEmail());
        }

        // Check for duplicate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username is already taken: " + request.getUsername());
        }

        // Find the role
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));

        // Generate email verification token
        String emailVerificationToken = UUID.randomUUID().toString();

        // Build user entity
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .roles(roles)
                .isActive(true)
                .isEmailVerified(false)
                .emailVerificationToken(emailVerificationToken)
                .emailVerificationTokenExpiry(LocalDateTime.now().plusHours(AppConstants.EMAIL_VERIFICATION_TOKEN_EXPIRY_HOURS))
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());

        // Send verification email
        emailService.sendEmailVerification(savedUser.getEmail(), emailVerificationToken);

        AuthResponse authResponse = AuthResponse.builder()
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .roles(savedUser.getRoles().stream()
                        .map(r -> r.getName().name())
                        .collect(Collectors.toSet()))
                .message("Registration successful. Please check your email to verify your account.")
                .build();

        return ApiResponse.success("Registration successful", authResponse);
    }

    /**
     * Authenticate user and generate JWT tokens.
     */
    @Transactional
    public ApiResponse<AuthResponse> login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        // Save refresh token in database
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDetails.getId()));
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshExpirationMs() / 1000));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getJwtExpirationMs() / 1000)
                .userId(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .fullName(userDetails.getFullName())
                .roles(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
                .build();

        log.info("User logged in successfully: {}", user.getEmail());
        return ApiResponse.success("Login successful", authResponse);
    }

    /**
     * Refresh access token using refresh token.
     */
    @Transactional
    public ApiResponse<AuthResponse> refreshToken(RefreshTokenRequest request) {
        User user = userRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (user.getRefreshTokenExpiry().isBefore(LocalDateTime.now())) {
            user.setRefreshToken(null);
            user.setRefreshTokenExpiry(null);
            userRepository.save(user);
            throw new TokenExpiredException("Refresh token has expired. Please login again.");
        }

        // Generate new tokens
        String roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.joining(","));

        String newAccessToken = jwtTokenProvider.generateAccessTokenFromUserId(
                user.getId(), user.getEmail(), user.getUsername(), roles);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken();

        // Update refresh token
        user.setRefreshToken(newRefreshToken);
        user.setRefreshTokenExpiry(LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshExpirationMs() / 1000));
        userRepository.save(user);

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getJwtExpirationMs() / 1000)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(user.getRoles().stream()
                        .map(r -> r.getName().name())
                        .collect(Collectors.toSet()))
                .build();

        return ApiResponse.success("Token refreshed successfully", authResponse);
    }

    /**
     * Logout user by invalidating refresh token.
     */
    @Transactional
    public ApiResponse<Void> logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setRefreshToken(null);
        user.setRefreshTokenExpiry(null);
        userRepository.save(user);

        SecurityContextHolder.clearContext();
        log.info("User logged out: {}", user.getEmail());
        return ApiResponse.success("Logged out successfully");
    }

    /**
     * Verify email using token.
     */
    @Transactional
    public ApiResponse<Void> verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid verification token"));

        if (user.getEmailVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Email verification token has expired. Please request a new one.");
        }

        user.setIsEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationTokenExpiry(null);
        userRepository.save(user);

        log.info("Email verified for user: {}", user.getEmail());
        return ApiResponse.success("Email verified successfully");
    }

    /**
     * Change password for authenticated user.
     */
    @Transactional
    public ApiResponse<Void> changePassword(Long userId, ChangePasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("New password cannot be the same as the current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        log.info("Password changed for user: {}", user.getEmail());
        return ApiResponse.success("Password changed successfully");
    }

    /**
     * Initiate forgot password flow.
     */
    @Transactional
    public ApiResponse<Void> forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(AppConstants.PASSWORD_RESET_TOKEN_EXPIRY_HOURS));
        userRepository.save(user);

        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

        log.info("Password reset email sent to: {}", user.getEmail());
        return ApiResponse.success("Password reset email sent. Please check your inbox.");
    }

    /**
     * Reset password using token.
     */
    @Transactional
    public ApiResponse<Void> resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }

        User user = userRepository.findByPasswordResetToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid password reset token"));

        if (user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Password reset token has expired. Please request a new one.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        userRepository.save(user);

        log.info("Password reset for user: {}", user.getEmail());
        return ApiResponse.success("Password reset successfully. You can now login with your new password.");
    }

    /**
     * Resend email verification token.
     */
    @Transactional
    public ApiResponse<Void> resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (user.getIsEmailVerified()) {
            throw new BadRequestException("Email is already verified");
        }

        String newToken = UUID.randomUUID().toString();
        user.setEmailVerificationToken(newToken);
        user.setEmailVerificationTokenExpiry(LocalDateTime.now().plusHours(AppConstants.EMAIL_VERIFICATION_TOKEN_EXPIRY_HOURS));
        userRepository.save(user);

        emailService.sendEmailVerification(user.getEmail(), newToken);

        return ApiResponse.success("Verification email sent. Please check your inbox.");
    }
}
