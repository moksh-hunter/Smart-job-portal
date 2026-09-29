package com.smartjobportal.util;

import com.smartjobportal.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
public final class FileUtils {

    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String PDF_CONTENT_TYPE = "application/pdf";

    /**
     * Validates that the uploaded file is a non-empty PDF within size limits.
     */
    public static void validatePdfFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required and cannot be empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equalsIgnoreCase(PDF_CONTENT_TYPE)) {
            throw new BadRequestException("Only PDF files are allowed");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size must not exceed 5MB");
        }
    }

    /**
     * Saves a file to the given directory, returning the saved file path as a String.
     */
    public static String saveFile(MultipartFile file, String uploadDir) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileName = UUID.randomUUID() + "_" + sanitizeFilename(file.getOriginalFilename());
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("File saved: {}", filePath);
            return filePath.toString();
        } catch (IOException e) {
            log.error("Failed to save file: {}", e.getMessage());
            throw new BadRequestException("Failed to save file: " + e.getMessage());
        }
    }

    /**
     * Deletes a file from the filesystem silently (no exception if missing).
     */
    public static void deleteFileSilently(String filePath) {
        if (filePath == null) return;
        try {
            Files.deleteIfExists(Paths.get(filePath));
            log.info("Deleted file: {}", filePath);
        } catch (IOException e) {
            log.warn("Could not delete file {}: {}", filePath, e.getMessage());
        }
    }

    /**
     * Strips potentially dangerous characters from a filename.
     */
    public static String sanitizeFilename(String filename) {
        if (filename == null) return "unnamed_file";
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    /**
     * Returns the file extension from a filename.
     */
    public static String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}
