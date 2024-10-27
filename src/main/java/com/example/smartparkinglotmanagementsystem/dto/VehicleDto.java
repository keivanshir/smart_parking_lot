package com.example.smartparkinglotmanagementsystem.dto;


import com.example.smartparkinglotmanagementsystem.enums.VehicleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class VehicleDto {

    private Long id;
    private String licensePlate;
    private VehicleType vehicleType;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private ParkingSpotDto assignedSpot;
}
