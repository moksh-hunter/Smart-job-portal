package com.smartjobportal.dto.company;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyResponse {
    private Long id;
    private Long recruiterId;
    private String recruiterName;
    private String companyName;
    private String website;
    private String industry;
    private String description;
    private String location;
    private String companySize;
    private Integer foundedYear;
    private String email;
    private String phoneNumber;
    private Boolean isVerified;
    private Integer totalJobs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
