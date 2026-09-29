package com.smartjobportal.util;

public final class AppConstants {

    private AppConstants() {
        throw new IllegalStateException("Utility class");
    }

    // Pagination defaults
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "desc";
    public static final int MAX_PAGE_SIZE = 50;

    // JWT
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    // Roles
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_RECRUITER = "ROLE_RECRUITER";
    public static final String ROLE_CANDIDATE = "ROLE_CANDIDATE";

    // File Upload
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    public static final String UPLOAD_DIR = "uploads/resumes";

    // Email
    public static final String EMAIL_VERIFICATION_SUBJECT = "Verify Your Email - Smart Job Portal";
    public static final String PASSWORD_RESET_SUBJECT = "Reset Your Password - Smart Job Portal";

    // Token Expiry
    public static final long EMAIL_VERIFICATION_TOKEN_EXPIRY_HOURS = 24;
    public static final long PASSWORD_RESET_TOKEN_EXPIRY_HOURS = 1;
}
