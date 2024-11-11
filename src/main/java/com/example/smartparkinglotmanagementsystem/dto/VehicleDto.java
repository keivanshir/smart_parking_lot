package com.example.smartparkinglotmanagementsystem.dto;


import com.example.smartparkinglotmanagementsystem.enums.VehicleType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleDto implements Serializable {

    private Long id;
    private String licensePlate;
    private VehicleType vehicleType;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private ParkingSpotDto assignedSpot;
}
