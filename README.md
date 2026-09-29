# 🚀 Smart Job Portal (Enterprise-Level Spring Boot Project)

A production-ready, clean-architecture enterprise job recruitment platform built with **Java 21**, **Spring Boot 3/4**, **Spring Security 6/7**, **JWT Authentication**, and **Hibernate/JPA**.

Designed to showcase end-to-end expertise in modern Java backend engineering, RESTful API design, dynamic query specifications, automated resume scoring, skill-gap analysis, and role-based access management.

---

## 🌟 Key Features

### 🔐 1. Authentication & Security
- **Role-Based Access Control (RBAC)**: `ADMIN`, `RECRUITER`, `CANDIDATE`.
- **JWT (JSON Web Token)**: Stateless session management with access & refresh token lifecycles.
- **BCrypt Password Hashing**: Salted secure hashing.
- **Account Actions**: Email verification, forgot/reset password with time-restricted tokens.

### 👤 2. Candidate Profiles & Resume Management
- Comprehensive profile tracking (personal, education, work experience, social links).
- **PDF Resume Upload & Download**: Validated file system storage with file type & size restrictions.
- **Dynamic Profile Completeness Scoring**: Automatic calculation of completeness percentage.

### 🏢 3. Company & Job Management
- Recruiter multi-company registration, profile editing, and verification status.
- Job postings with granular salary ranges, required experience, skill tags, employment types (`FULL_TIME`, `REMOTE`, etc.), and application deadlines.
- Status management: `OPEN`, `CLOSED`, `PAUSED`, `DRAFT`.

### 🔍 4. Advanced Job Search & Dynamic Filtering
- Powered by Spring Data JPA **Specifications**.
- Filter seamlessly by title, location, salary range, experience, employment type, remote availability, and skill overlap with pagination and sorting.

### 📝 5. Application & Interview Lifecycle
- Candidate job applications with resume attachment and custom cover letters.
- Candidate application withdrawal & job bookmarking/saving.
- Application status transitions: `APPLIED` ➔ `UNDER_REVIEW` ➔ `SHORTLISTED` ➔ `INTERVIEW_SCHEDULED` ➔ `SELECTED` / `REJECTED`.
- Multi-round interview scheduling with interviewer details, meeting links, status tracking, and 5-star rating/feedback submission.

### 🤖 6. Smart Features & AI/ATS Capabilities
- **ATS Resume Scoring (0–100)**: Evaluates profile completeness, skill matches, experience suitability, and education match against job specifications.
- **Skill Gap Detection**: Case-insensitive comparison highlighting matched skills, missing skills, and actionable improvement suggestions.
- **Job Recommendations**: Automatic job matching based on candidate profile skills (>30% match threshold).
- **Candidate Ranking**: Automatic sorting of applicants by composite ranking score.

### 📊 7. Analytics & Audit Logging
- **Admin Dashboard**: Aggregated user counts, active recruiters, job distributions, and application status metrics.
- **Recruiter Dashboard**: Total applicants, interview tallies, shortlisted candidates, and ATS score averages.
- **Audit Logging**: Comprehensive activity tracking across users and administrative actions.

---

## 🛠️ Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4.0.1
- **Security:** Spring Security 7.x, JJWT (io.jsonwebtoken 0.12.6)
- **Persistence:** Spring Data JPA, Hibernate, MySQL 8
- **Documentation:** Swagger UI / OpenAPI 3 (SpringDoc)
- **Utilities:** Lombok, Jakarta Validation

---

## 🏗️ Architecture & Package Structure

```
com.smartjobportal
├── config          # Security, OpenAPI, Async & Data initializers
├── controller      # REST API Controllers (10 controllers)
├── dto             # Data Transfer Objects (auth, job, application, resume, admin, etc.)
├── entity          # JPA Entities and Enums (User, Job, Application, Interview, etc.)
├── exception       # GlobalExceptionHandler & custom exceptions
├── mapper          # Entity-to-DTO and DTO-to-Entity mappers
├── repository      # Spring Data JPA repositories with query specifications
├── security        # CustomUserDetails, JWT filter, TokenProvider, EntryPoint
├── service         # Business logic layer (10 enterprise services)
├── specification   # Dynamic JPA Criteria specifications
└── util            # ATS calculator, skill matching engine, pagination utilities
```

---

## ⚙️ Getting Started

### Prerequisites
- **Java 21** or higher
- **Maven 3.8+** (or use included `mvnw`)
- **MySQL 8.0+**

### Database Setup
Ensure MySQL is running and create the database (or let Hibernate create it):
```sql
CREATE DATABASE IF NOT EXISTS smart_job_portal;
```

Update your `src/main/resources/application.properties` if needed:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smart_job_portal?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
```

### Build & Run
```bash
# Build the project
./mvnw clean compile

# Run the Spring Boot application
./mvnw spring-boot:run
```

---

## 📖 API Documentation (Swagger)

Once the application is started, access the interactive Swagger OpenAPI UI at:
- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **API Docs (JSON):** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Default seeded administrator credentials:
- **Email:** `admin@smartjobportal.com`
- **Password:** `Admin@123`
