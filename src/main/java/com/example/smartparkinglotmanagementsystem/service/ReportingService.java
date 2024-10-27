package com.example.smartparkinglotmanagementsystem.service;

import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.enums.Frequency;
import org.springframework.data.domain.Pageable;

public interface ReportingService {
    Response generateReportOfAllParkedVehicles(Pageable pageable);
    Response generateRevenueReport(Frequency frequency, Pageable pageable);
}
