package com.example.smartparkinglotmanagementsystem.specification;

import com.example.smartparkinglotmanagementsystem.entity.Revenue;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class RevenueSpecification {

    public static Specification<Revenue> createdBetween(LocalDateTime startDate, LocalDateTime endDate){
        return ((root, query, criteriaBuilder) -> {
            if (startDate != null && endDate!=null){
                return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
            } else if (startDate != null){
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate);
            } else if (endDate != null){
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate);
            }else {
                return null;
            }
        });
    }
}
