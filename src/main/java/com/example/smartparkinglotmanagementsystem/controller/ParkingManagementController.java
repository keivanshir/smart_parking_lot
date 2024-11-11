package com.example.smartparkinglotmanagementsystem.controller;

import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.entity.ParkingSpot;
import com.example.smartparkinglotmanagementsystem.repository.ParkingSpotRepository;
import com.example.smartparkinglotmanagementsystem.service.ParkingManagementService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping("/management")
@AllArgsConstructor
public class ParkingManagementController {

    private final ParkingManagementService parkingManagementService;
    private final ParkingSpotRepository parkingSpotRepository;

    @GetMapping("/parkingSpotsStat")
    @Operation(summary = "view parking spots status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "shows all parking spots")
            }
    )
    @RateLimiter(name = "parkingSystem")
    public ResponseEntity<Response> viewRealtimeStatus(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue= "1000") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<ParkingSpot> parkingSpotPage = parkingSpotRepository.findAll(pageable);

        return new ResponseEntity<>(Response.builder()
                .status(200)
                .message("All parking spots")
                .parkingSpotDtoList(parkingManagementService.viewRealtimeStatus(parkingSpotPage))
                .totalPage(parkingSpotPage.getTotalPages())
                .totalElement(parkingSpotPage.getTotalElements())
                .build(), HttpStatus.OK);
    }

    @GetMapping("/occupiedParkingSpots")
    @Operation(summary = "view filled parking spots",
            responses = {
                    @ApiResponse(responseCode = "200", description = "shows filled parking spots"),
                    @ApiResponse(responseCode = "404", description = "all Parking spots are vacant")
            }
    )
    @RateLimiter(name = "parkingSystem")
    public ResponseEntity<Response> viewOccupancyRate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue= "1000") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return new ResponseEntity<>(parkingManagementService.viewOccupancyRate(pageable), HttpStatus.OK);
    }
}
