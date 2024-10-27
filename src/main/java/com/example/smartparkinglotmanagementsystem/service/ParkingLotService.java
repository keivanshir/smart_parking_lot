package com.example.smartparkinglotmanagementsystem.service;

import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.dto.VehicleDto;

public interface ParkingLotService {
    Response registerVehicle(VehicleDto vehicleDto);
    Response registerVehicleExit(Long id);

}
