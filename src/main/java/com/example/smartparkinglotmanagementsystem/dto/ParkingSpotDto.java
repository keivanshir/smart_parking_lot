package com.example.smartparkinglotmanagementsystem.dto;

import com.example.smartparkinglotmanagementsystem.enums.SpotSize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ParkingSpotDto {
    private Long id;

    private String spotId;

    private SpotSize spotSize;

    private Boolean isOccupied;

    private VehicleDto currentVehicle;
}
