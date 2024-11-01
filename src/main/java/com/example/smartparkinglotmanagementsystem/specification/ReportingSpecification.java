package com.example.smartparkinglotmanagementsystem.specification;

import com.example.smartparkinglotmanagementsystem.entity.ParkingSpot;
import com.example.smartparkinglotmanagementsystem.entity.Vehicle;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ReportingSpecification {

    public static Specification<Vehicle> parkedCars(){
        return (root, query, criteriaBuilder) -> {
            Join<ParkingSpot, Vehicle> parkingSpotVehicleJoin = root.join("assignedSpot", JoinType.INNER);

          return criteriaBuilder.and(
                  criteriaBuilder.isNotNull(parkingSpotVehicleJoin.get("id")),
                  criteriaBuilder.equal(parkingSpotVehicleJoin.get("isOccupied"), true));

        };

    }
}
