package com.smartjobportal.dto.company;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest {

    @NotBlank(message = "Company name is required")
    @Size(max = 200, message = "Company name must not exceed 200 characters")
    private String companyName;

    @Pattern(regexp = "^https?://.*", message = "Invalid website URL")
    private String website;

    @Size(max = 100)
    private String industry;

    private String description;

    @Size(max = 200)
    private String location;

    private String companySize;
    private Integer foundedYear;

    @Email
    private String email;

    @Size(max = 15)
    private String phoneNumber;
}
