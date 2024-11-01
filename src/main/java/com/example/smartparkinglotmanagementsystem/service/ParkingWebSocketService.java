package com.example.smartparkinglotmanagementsystem.service;

import com.example.smartparkinglotmanagementsystem.dto.ParkingSpotDto;

public interface ParkingWebSocketService {
    void sendOccupancyUpdate(ParkingSpotDto parkingSpotDto);
}
