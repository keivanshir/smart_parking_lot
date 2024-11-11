package com.example.smartparkinglotmanagementsystem.service;

import com.example.smartparkinglotmanagementsystem.dto.ParkingSpotDto;
import com.example.smartparkinglotmanagementsystem.dto.Response;

public interface ParkingSpotService {
    ParkingSpotDto freeUpParkingSpot(Long id);

    ParkingSpotDto addParkingSpot(ParkingSpotDto parkingSpotDto);
    Response deleteParkingSpot(Long id);

}
