package com.example.smartparkinglotmanagementsystem.dto;

import com.example.smartparkinglotmanagementsystem.enums.SpotSize;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParkingSpotDto {
    private Long id;

    private String spotId;

    private SpotSize spotSize;

    private Boolean isOccupied;

    private VehicleDto currentVehicle;
}
