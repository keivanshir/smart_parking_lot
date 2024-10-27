package com.example.smartparkinglotmanagementsystem.repository;

import com.example.smartparkinglotmanagementsystem.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long>, JpaSpecificationExecutor<ParkingSpot>{
}
