package com.example.smartparkinglotmanagementsystem.repository;

import com.example.smartparkinglotmanagementsystem.entity.ParkingSpot;
import com.example.smartparkinglotmanagementsystem.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long>, JpaSpecificationExecutor<ParkingSpot>{
    Optional<ParkingSpot> findParkingSpotByCurrentVehicle(Vehicle currentVehicle);
}
