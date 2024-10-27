package com.example.smartparkinglotmanagementsystem.service;

import com.example.smartparkinglotmanagementsystem.dto.ParkingSpotDto;
import com.example.smartparkinglotmanagementsystem.dto.Response;

public interface ParkingSpotService {
    Response freeUpParkingSpot(Long id);

    Response addParkingSpot(ParkingSpotDto parkingSpotDto);
    Response deleteParkingSpot(Long id);

}
