package com.example.smartparkinglotmanagementsystem.service;

import com.example.smartparkinglotmanagementsystem.dto.Response;
import org.springframework.data.domain.Pageable;

public interface ParkingManagementService {
    Response viewRealtimeStatus(Pageable pageable);
    Response viewOccupancyRate(Pageable pageable);

}
