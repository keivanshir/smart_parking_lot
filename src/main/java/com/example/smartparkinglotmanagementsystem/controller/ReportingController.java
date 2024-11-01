package com.example.smartparkinglotmanagementsystem.controller;

import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.enums.Frequency;
import com.example.smartparkinglotmanagementsystem.service.ReportingService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@AllArgsConstructor
public class ReportingController {

    private final ReportingService reportingService;

    @GetMapping("/parkedVehicles")
    public ResponseEntity<Response> generateReportOfAllParkedVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return new ResponseEntity<>(reportingService.generateReportOfAllParkedVehicles(pageable), HttpStatus.OK);
    }

    @GetMapping("/revenues")
    public ResponseEntity<Response> getRevenueByFrequency(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size,
            @RequestParam(required = false) String frequency
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Frequency freq = frequency != null ? Frequency.valueOf(frequency.toUpperCase()) : null;
        return new ResponseEntity<>(reportingService.generateRevenueReport(freq, pageable), HttpStatus.OK);
    }
}
