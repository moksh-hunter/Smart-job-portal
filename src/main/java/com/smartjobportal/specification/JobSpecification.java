package com.smartjobportal.specification;

import com.smartjobportal.entity.Job;
import com.smartjobportal.entity.EmploymentType;
import com.smartjobportal.entity.JobStatus;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class JobSpecification {

    public static Specification<Job> byTitle(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Job> byLocation(String location) {
        return (root, query, cb) -> {
            if (location == null || location.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
        };
    }

    public static Specification<Job> byCompanyId(Long companyId) {
        return (root, query, cb) -> {
            if (companyId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("company").get("id"), companyId);
        };
    }

    public static Specification<Job> byEmploymentType(EmploymentType type) {
        return (root, query, cb) -> {
            if (type == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("employmentType"), type);
        };
    }

    public static Specification<Job> byStatus(JobStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Job> byMinSalary(Double minSalary) {
        return (root, query, cb) -> {
            if (minSalary == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("minSalary"), minSalary);
        };
    }

    public static Specification<Job> byMaxSalary(Double maxSalary) {
        return (root, query, cb) -> {
            if (maxSalary == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("maxSalary"), maxSalary);
        };
    }

    public static Specification<Job> byExperience(Double experience) {
        return (root, query, cb) -> {
            if (experience == null) {
                return cb.conjunction();
            }
            Predicate minExp = cb.lessThanOrEqualTo(root.get("minExperience"), experience);
            Predicate maxExp = cb.greaterThanOrEqualTo(root.get("maxExperience"), experience);
            return cb.and(minExp, maxExp);
        };
    }

    public static Specification<Job> bySkills(List<String> skills) {
        return (root, query, cb) -> {
            if (skills == null || skills.isEmpty()) {
                return cb.conjunction();
            }
            
            var join = root.join("requiredSkills", JoinType.INNER);
            Predicate inClause = join.in(skills);
            query.distinct(true);
            
            return inClause;
        };
    }

    public static Specification<Job> byIsRemote(Boolean isRemote) {
        return (root, query, cb) -> {
            if (isRemote == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("isRemote"), isRemote);
        };
    }
}
