package com.tlu.EmployeeManagement.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.tlu.EmployeeManagement.entity.Notification;

import jakarta.persistence.criteria.Predicate;

public class NotificationSpecification {

    public static Specification<Notification> filterNotifications(
            Integer deptId,
            String search) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Always filter out deleted notifications
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));

            // Filter by department
            if (deptId != null) {
                predicates.add(criteriaBuilder.equal(root.get("deptId"), deptId));
            }

            // Search in title or content (case-insensitive)
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    searchPattern
                );
                Predicate contentPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("content")),
                    searchPattern
                );
                predicates.add(criteriaBuilder.or(titlePredicate, contentPredicate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
