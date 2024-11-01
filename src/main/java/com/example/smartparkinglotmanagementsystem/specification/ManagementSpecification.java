package com.example.smartparkinglotmanagementsystem.specification;

import com.example.smartparkinglotmanagementsystem.entity.ParkingSpot;
import org.springframework.data.jpa.domain.Specification;

public class ManagementSpecification {
    public static Specification<ParkingSpot> isOccupiedSpot(){
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.isNotNull(root.get("currentVehicle")),
                criteriaBuilder.equal(root.get("isOccupied"), true)
        );
    }
}
