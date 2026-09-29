package com.smartjobportal.config;

import com.smartjobportal.entity.Role;
import com.smartjobportal.entity.RoleName;
import com.smartjobportal.entity.User;
import com.smartjobportal.repository.RoleRepository;
import com.smartjobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        initializeRoles();
        initializeAdminUser();
    }

    private void initializeRoles() {
        Arrays.stream(RoleName.values()).forEach(roleName -> {
            if (!roleRepository.existsByName(roleName)) {
                Role role = Role.builder()
                        .name(roleName)
                        .description(getDescription(roleName))
                        .build();
                roleRepository.save(role);
                log.info("Role created: {}", roleName);
            }
        });
    }

    private void initializeAdminUser() {
        if (!userRepository.existsByEmail("admin@smartjobportal.com")) {
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            User admin = User.builder()
                    .username("admin")
                    .email("admin@smartjobportal.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .fullName("System Administrator")
                    .isActive(true)
                    .isEmailVerified(true)
                    .roles(Set.of(adminRole))
                    .build();

            userRepository.save(admin);
            log.info("Admin user created: admin@smartjobportal.com / Admin@123");
        }
    }

    private String getDescription(RoleName roleName) {
        return switch (roleName) {
            case ROLE_ADMIN -> "System Administrator with full access";
            case ROLE_RECRUITER -> "Recruiter who can post jobs and manage applicants";
            case ROLE_CANDIDATE -> "Job seeker who can search and apply for jobs";
        };
    }
}
