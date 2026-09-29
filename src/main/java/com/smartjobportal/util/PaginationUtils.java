package com.smartjobportal.util;

import com.smartjobportal.exception.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PaginationUtils {

    private PaginationUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Builds a Pageable from page/size/sort parameters with safety capping.
     */
    public static Pageable buildPageable(int page, int size, String sortBy, String sortDir) {
        // Safety caps
        if (page < 0) page = 0;
        if (size < 1) size = Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);
        if (size > AppConstants.MAX_PAGE_SIZE) size = AppConstants.MAX_PAGE_SIZE;

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return PageRequest.of(page, size, sort);
    }

    /**
     * Validates and parses page number from a string.
     */
    public static int parsePage(String pageStr) {
        try {
            int page = Integer.parseInt(pageStr);
            if (page < 0) throw new BadRequestException("Page number cannot be negative");
            return page;
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid page number: " + pageStr);
        }
    }

    /**
     * Validates and parses page size from a string.
     */
    public static int parseSize(String sizeStr) {
        try {
            int size = Integer.parseInt(sizeStr);
            if (size < 1) throw new BadRequestException("Page size must be at least 1");
            if (size > AppConstants.MAX_PAGE_SIZE)
                throw new BadRequestException("Page size cannot exceed " + AppConstants.MAX_PAGE_SIZE);
            return size;
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid page size: " + sizeStr);
        }
    }
}
