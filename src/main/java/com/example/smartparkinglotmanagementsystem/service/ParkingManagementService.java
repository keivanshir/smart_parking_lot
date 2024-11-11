package com.example.smartparkinglotmanagementsystem.service;

import com.example.smartparkinglotmanagementsystem.dto.ParkingSpotDto;
import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.entity.ParkingSpot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParkingManagementService {
    List<ParkingSpotDto> viewRealtimeStatus(Page<ParkingSpot> parkingSpotPage);
    Response viewOccupancyRate(Pageable pageable);

}
