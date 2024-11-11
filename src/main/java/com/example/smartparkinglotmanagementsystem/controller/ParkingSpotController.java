package com.example.smartparkinglotmanagementsystem.controller;

import com.example.smartparkinglotmanagementsystem.dto.ParkingSpotDto;
import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.service.ParkingSpotService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkingSpot")
@AllArgsConstructor
public class ParkingSpotController {

    private ParkingSpotService parkingSpotService;


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "deletes a parking spot and notifies removed spot (ADMIN only)",
            responses = {
                @ApiResponse(responseCode = "200", description = "parking spot deleted successfully"),
                @ApiResponse(responseCode = "404", description = "Parking spot not found for given id")
            })
    @RateLimiter(name = "parkingSystem")
    public ResponseEntity<Response> deleteParkingSpot(@PathVariable Long id){
        return new ResponseEntity<>(parkingSpotService.deleteParkingSpot(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "adds a parking spot and notifies empty spot (ADMIN only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "parking spot added successfully")
            })
    @RateLimiter(name = "parkingSystem")
    public ResponseEntity<Response> addParkingSpot(@RequestBody ParkingSpotDto parkingSpotDto){
        return new ResponseEntity<>(Response.builder().parkingSpot(parkingSpotService.addParkingSpot(parkingSpotDto)).build(), HttpStatus.CREATED);
    }

}
