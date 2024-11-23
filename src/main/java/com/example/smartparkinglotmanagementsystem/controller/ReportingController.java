package com.example.smartparkinglotmanagementsystem.controller;

import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.enums.Frequency;
import com.example.smartparkinglotmanagementsystem.service.ReportingService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "Authorization")
public class ReportingController {

    private final ReportingService reportingService;

    @GetMapping("/parkedVehicles")
    @Operation(summary = "generates report of all parked vehicles",
            responses = {
                    @ApiResponse(responseCode = "200", description = "shows all parked vehicles"),
                    @ApiResponse(responseCode = "404", description = "no parked vehicle found")
            })
    @RateLimiter(name = "parkingSystem")
    public ResponseEntity<Response> generateReportOfAllParkedVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return new ResponseEntity<>(reportingService.generateReportOfAllParkedVehicles(pageable), HttpStatus.OK);
    }

    @GetMapping("/revenues")
    @Operation(summary = "generates revenue by frequency",
            responses = {
                    @ApiResponse(responseCode = "200", description = "shows revenue of vehicles parked in the past"),
                    @ApiResponse(responseCode = "404", description = "revenues not found")
            })
    @RateLimiter(name = "parkingSystem")
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
